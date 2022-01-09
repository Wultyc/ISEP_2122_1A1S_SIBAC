package pt.ipp.isep.dei.kbs.bjt;

import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;
import pt.ipp.isep.dei.math.Calculator;
import pt.ipp.isep.dei.model.Evidence;
import pt.ipp.isep.dei.model.NumericEvidence;
import pt.ipp.isep.dei.model.Hypothesis;
import pt.ipp.isep.dei.model.helpers.Multiplier;
import pt.ipp.isep.dei.model.helpers.NumericAlternative;
import pt.ipp.isep.dei.model.helpers.NumericValue;
import pt.ipp.isep.dei.repository.iRepository;
import pt.ipp.isep.dei.Main;

import java.math.BigDecimal;

public class BTJStatus {

    private KieSession KS;
    private TrackingAgendaEventListener agendaEventListener;
    private iRepository repository;

    public BTJStatus(KieSession KS, TrackingAgendaEventListener agendaEventListener, iRepository repository) {
        this.KS = KS;
        this.agendaEventListener = agendaEventListener;
        this.repository = repository;
    }

    public boolean testCutOverZone(){

        Main.LOGGER.info("Starting testing Cut Over Zone");

        NumericEvidence VBB = this.repository.retrieveEvidence(NumericEvidence.VBB);
        NumericEvidence VBE_ON = this.repository.retrieveEvidence(NumericEvidence.VBE_ON);

        boolean validation = Calculator.lowerThan(VBB.getNormValue(), VBE_ON.getNormValue());

        if(validation) {
            this.agendaEventListener.addLhs(VBB);
            this.agendaEventListener.addLhs(VBE_ON);
        } else {
            this.KS.insert(new Evidence(Evidence.TBJ_IN_CUT_OVER_ZONE, "NO"));
        }

        Main.LOGGER.info("Cut Over Zone returned " + validation);

        return validation;
    }

    public boolean testActiveZone(){

        Main.LOGGER.info("Starting testing Active Zone");

        NumericEvidence VCC = this.repository.retrieveEvidence(NumericEvidence.VCC);
        NumericEvidence VBB = this.repository.retrieveEvidence(NumericEvidence.VBB);
        NumericEvidence VBE = this.repository.retrieveEvidence(NumericEvidence.VBE_ON);
        NumericEvidence RBB = this.repository.retrieveEvidence(NumericEvidence.RBB);
        NumericEvidence RC = this.repository.retrieveEvidence(NumericEvidence.RC);
        NumericEvidence RE = this.repository.retrieveEvidence(NumericEvidence.RE);
        NumericEvidence beta = this.repository.retrieveEvidence(NumericEvidence.BJT_GAIN);

        boolean validation = false;

        BigDecimal ib = Calculator.fraction(
                Calculator.subtract(VBB.getNormValue(),VBE.getNormValue()),
                Calculator.sum(RBB.getNormValue(), Calculator.multiply(beta.getNormValue()), RE.getNormValue())
        );
        NumericEvidence IB = generateEvidence(ib, NumericEvidence.IB);

        BigDecimal ic = Calculator.multiply(beta.getNormValue(), ib);
        NumericEvidence IC = generateEvidence(ic, NumericEvidence.IC);

        BigDecimal vce = Calculator.subtract(VCC.getNormValue(), Calculator.multiply(ic, Calculator.sum(RC.getNormValue(), RE.getNormValue())));
        NumericEvidence VCE = generateEvidence(vce, NumericEvidence.VCE);

        validation = Calculator.greaterThanOrEqual(VCC.getNormValue(),vce) && Calculator.greaterThanOrEqual(vce,VBE.getNormValue());

        if(validation) {
            this.KS.insert(IB);
            this.KS.insert(IC);
            this.KS.insert(VCE);
            this.agendaEventListener.addLhs(IB);
            this.agendaEventListener.addLhs(IC);
            this.agendaEventListener.addLhs(VCE);
        } else {
            this.KS.insert(new Evidence(Evidence.TBJ_IN_ACTIVE_ZONE, "NO"));
        }

        Main.LOGGER.info("Active Zone returned " + validation);

        return validation;
    }

    public boolean testSaturationZone(){

        Main.LOGGER.info("Starting testing Saturation Zone");

        NumericEvidence VCC = this.repository.retrieveEvidence(NumericEvidence.VCC);
        NumericEvidence VBB = this.repository.retrieveEvidence(NumericEvidence.VBB);
        NumericEvidence VBE = this.repository.retrieveEvidence(NumericEvidence.VBE_ON);
        NumericEvidence RBB = this.repository.retrieveEvidence(NumericEvidence.RBB);
        NumericEvidence RC = this.repository.retrieveEvidence(NumericEvidence.RC);
        NumericEvidence RE = this.repository.retrieveEvidence(NumericEvidence.RE);

        BigDecimal ten = new BigDecimal("10");
        BigDecimal zero = new BigDecimal("0");
        int nrTests = (int) VBE.getNormValue().doubleValue() * 10;

        BigDecimal vce_sat;
        BigDecimal ic = zero;
        BigDecimal ib = zero;
        BigDecimal icCopy = zero;
        BigDecimal ibCopy = zero;

        boolean validation = false;

        for (int i = 1; i <= nrTests; i++ ){
            vce_sat = Calculator.fraction(BigDecimal.valueOf(i), ten);

            ic = Calculator.fraction(
                    Calculator.subtract(VCC.getNormValue(), vce_sat),
                    Calculator.sum(RE.getNormValue(), RC.getNormValue())
            );

            if (Calculator.lowerThanOrEqual(ic, zero))
                continue;

            ib = Calculator.fraction(
                    Calculator.subtract(VBB.getNormValue(),VBE.getNormValue(),Calculator.multiply(ic, RC.getNormValue())),
                    RBB.getNormValue()
            );

            validation = validation || (ib.compareTo(zero) <= 0);

            if(Calculator.greaterThan(ib, zero)){
                validation = true;
                ibCopy = ib;
                icCopy = ic;
            }
        }

        if(validation) {
            NumericEvidence IB = generateEvidence(ibCopy, NumericEvidence.IB);
            NumericEvidence IC = generateEvidence(icCopy, NumericEvidence.IC);

            this.KS.insert(IB);
            this.KS.insert(IC);
            this.agendaEventListener.addLhs(IB);
            this.agendaEventListener.addLhs(IC);
        } else {
            this.KS.insert(new Evidence(Evidence.TBJ_IN_SATURATION_ZONE, "NO"));
        }

        Main.LOGGER.info("Saturation Zone returned " + validation);

        return validation;
    }

    public boolean hasMoreHypothesisToTest(){
        int count = countNrFacts(Hypothesis.class);

        Main.LOGGER.info("Where tested already " + count + " Hypothesis");

        return count < 3;
    }

    public int countNrFacts(Class<?> factType){
        return this.KS.getObjects(new ClassObjectFilter(factType)).size();
    }

    public void chooseNewHypothesis(){
        Hypothesis newHypothesis = this.repository.chooseNewHypothesis();
        this.KS.insert(newHypothesis);
    }

    private NumericEvidence generateEvidence(BigDecimal value, NumericAlternative alternative){
        NumericValue tmpNV = new NumericValue(value, new Multiplier(), alternative.getUnit());

        tmpNV.applyBestMultiplier();

        return new NumericEvidence(alternative, tmpNV.getValueToHuman(), tmpNV);
    }
}
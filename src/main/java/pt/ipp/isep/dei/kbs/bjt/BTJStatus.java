package pt.ipp.isep.dei.kbs.bjt;

import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;
import pt.ipp.isep.dei.math.Calculator;
import pt.ipp.isep.dei.model.Evidence;
import pt.ipp.isep.dei.model.Hypothesis;
import pt.ipp.isep.dei.model.helpers.Multiplier;
import pt.ipp.isep.dei.model.helpers.NumericAlternative;
import pt.ipp.isep.dei.model.helpers.NumericValue;
import pt.ipp.isep.dei.repository.iRepository;
import pt.ipp.isep.dei.Main;

import java.math.BigDecimal;
import java.util.Collection;

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

        Evidence VBB = this.repository.retrieveEvidence(Evidence.VBB);
        Evidence VBE_ON = this.repository.retrieveEvidence(Evidence.VBE_ON);

        boolean validation = Calculator.lowerThan(VBB.getNormValue(), VBE_ON.getNormValue());

        if(validation) {
            this.agendaEventListener.addLhs(VBB);
            this.agendaEventListener.addLhs(VBE_ON);
        }

        Main.LOGGER.info("Cut Over Zone returned " + validation);

        return validation;
    }

    public boolean testActiveZone(){

        Main.LOGGER.info("Starting testing Active Zone");

        Evidence VCC = this.repository.retrieveEvidence(Evidence.VCC);
        Evidence VBB = this.repository.retrieveEvidence(Evidence.VBB);
        Evidence VBE = this.repository.retrieveEvidence(Evidence.VBE_ON);
        Evidence RBB = this.repository.retrieveEvidence(Evidence.RBB);
        Evidence RC = this.repository.retrieveEvidence(Evidence.RC);
        Evidence RE = this.repository.retrieveEvidence(Evidence.RE);
        Evidence beta = this.repository.retrieveEvidence(Evidence.BJT_GAIN);

        boolean validation = false;

        BigDecimal ib = Calculator.fraction(
                Calculator.subtract(VBB.getNormValue(),VBE.getNormValue()),
                Calculator.sum(RBB.getNormValue(), Calculator.multiply(beta.getNormValue()), RE.getNormValue())
        );
        Evidence IB = generateEvidence(ib, Evidence.IB);

        BigDecimal ic = Calculator.multiply(beta.getNormValue(), ib);
        Evidence IC = generateEvidence(ic, Evidence.IC);

        BigDecimal vce = Calculator.subtract(VCC.getNormValue(), Calculator.multiply(ic, Calculator.sum(RC.getNormValue(), RE.getNormValue())));
        Evidence VCE = generateEvidence(vce, Evidence.VCE);

        validation = Calculator.greaterThanOrEqual(VCC.getNormValue(),vce) && Calculator.greaterThanOrEqual(vce,VBE.getNormValue());

        if(validation) {
            this.KS.insert(IB);
            this.KS.insert(IC);
            this.KS.insert(VCE);
            this.agendaEventListener.addLhs(IB);
            this.agendaEventListener.addLhs(IC);
            this.agendaEventListener.addLhs(VCE);
        }

        Main.LOGGER.info("Active Zone returned " + validation);

        return validation;
    }

    public boolean testSaturationZone(){

        Main.LOGGER.info("Starting testing Saturation Zone");

        Evidence VCC = this.repository.retrieveEvidence(Evidence.VCC);
        Evidence VBB = this.repository.retrieveEvidence(Evidence.VBB);
        Evidence VBE = this.repository.retrieveEvidence(Evidence.VBE_ON);
        Evidence RBB = this.repository.retrieveEvidence(Evidence.RBB);
        Evidence RC = this.repository.retrieveEvidence(Evidence.RC);
        Evidence RE = this.repository.retrieveEvidence(Evidence.RE);

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
            Evidence IB = generateEvidence(ibCopy, Evidence.IB);
            Evidence IC = generateEvidence(icCopy, Evidence.IC);

            this.KS.insert(IB);
            this.KS.insert(IC);
            this.agendaEventListener.addLhs(IB);
            this.agendaEventListener.addLhs(IC);
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

    private Evidence generateEvidence(BigDecimal value, NumericAlternative alternative){
        NumericValue tmpNV = new NumericValue(value, new Multiplier(), alternative.getUnit());

        tmpNV.applyBestMultiplier();

        return new Evidence(alternative, tmpNV.getValueToHuman(), tmpNV);
    }
}
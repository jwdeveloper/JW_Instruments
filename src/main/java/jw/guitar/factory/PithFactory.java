package jw.guitar.factory;

import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PithFactory {


    private static List<FretsTuning> instnace;

    public static List<FretsTuning> getPitch() {
        if (instnace == null) {
            instnace = doPith();
        }
        return instnace;
    }

    public static List<FretsTuning> doPith() {
        //https://stackoverflow.com/questions/54879626/python-guitar-fretboard-pitch-frequency-implementation
        double SEMITONE_STEP = Math.pow(2, (1f / 12f));
        var LOW_E_FREQ = 82.4f;
        var N_FRETS = 24;
        var fresq = new ArrayList<FretsTuning>();
        for (var i = 0; i < 6; i++) {
            var fred = new FretsTuning(N_FRETS);
            if (i == 1) {
                fred.setStringSteps(4);
            } else {
                fred.setStringSteps(5);
            }
            fresq.add(fred);
        }

        var e6 = fresq.get(fresq.size() - 1);
        for (var i = 0; i < N_FRETS; i++) {

            e6.getTunnings()[i] = LOW_E_FREQ * (Math.pow(SEMITONE_STEP, i));
        }

        for (var i = fresq.size() - 2; i >= 0; i--) {
            var fred = fresq.get(i);
            var base_freq = fresq.get(i + 1).getTunnings()[fred.getStringSteps()];
            for (var j = 0; j < N_FRETS; j++) {
                fred.getTunnings()[j] = base_freq * (Math.pow(SEMITONE_STEP, j));
            }
        }


        /*for (var fred : fresq) {
            var string = new StringBuilder();
            var df = new DecimalFormat("#.#");
            for (var a : fred.getTunnings()) {
                var res = df.format(a);
                string.append(res).append(" - ");
            }
        }*/
        return fresq;
    }

    @Data
    public static class FretsTuning {
        public FretsTuning(int frends) {
            tunnings = new double[frends];
        }

        private int stringSteps;

        private double[] tunnings;
    }
}

import static java.lang.System.exit;

public class Pauvocoder {

    // Processing SEQUENCE size (100 msec with 44100Hz samplerate)
    final static int SEQUENCE = StdAudio.SAMPLE_RATE/10;

    // Overlapping size (20 msec)
    final static int OVERLAP = SEQUENCE/5 ;
    // Best OVERLAP offset seeking window (15 msec)
    final static int SEEK_WINDOW = 3*OVERLAP/4;

    public static void main(String[] args) {
        if (args.length < 2)
        {
            System.out.println("usage: pauvocoder <input.wav> <freqScale>\n");
            exit(1);
        }


        String wavInFile = args[0];
        double freqScale = Double.valueOf(args[1]);
        String outPutFile= wavInFile.split("\\.")[0] + "_" + freqScale +"_";

        // Open input .wev file
        double[] inputWav = StdAudio.read(wavInFile);

        // Resample test
        double[] newPitchWav = resample(inputWav, freqScale);
        StdAudio.save(outPutFile+"Resampled.wav", newPitchWav);

        // Simple dilatation
        double[] outputWav   = vocodeSimple(newPitchWav, 1.0/freqScale);
        StdAudio.save(outPutFile+"Simple.wav", outputWav);

        // Simple dilatation with overlaping
        outputWav = vocodeSimpleOver(newPitchWav, 1.0/freqScale);
        StdAudio.save(outPutFile+"SimpleOver.wav", outputWav);

        // Simple dilatation with overlaping and maximum cross correlation search
        outputWav = vocodeSimpleOverCross(newPitchWav, 1.0/freqScale);
        StdAudio.save(outPutFile+"SimpleOverCross.wav", outputWav);

        joue(outputWav);

        // Some echo above all
        outputWav = echo(outputWav, 100, 0.7);
        StdAudio.save(outPutFile+"SimpleOverCrossEcho.wav", outputWav);

        // Display waveform
        displayWaveform(outputWav);

    }

    /**
     * Resample inputWav with freqScale
     * @param inputWav
     * @param freqScale
     * @return resampled wav
     */
    public static double[] resample(double[] inputWav, double freqScale) {
        int tailleOutput = (int) Math.round(inputWav.length / freqScale);
        double[] output = new double[tailleOutput];
        for (int i = 0 ; i < tailleOutput ; i++){
            output[i] = inputWav[(int)(i * freqScale)];
        }
        return output;
    }

    /**
     * Simple dilatation, without any overlapping
     * @param inputWav
     * @param dilatation factor
     * @return dilated wav
     */
    public static double[] vocodeSimple(double[] inputWav, double dilatation) {
        double[] outputWav = new double[(int) Math.round(inputWav.length / dilatation)];
        int saut = (int) Math.round(SEQUENCE * dilatation);
        int nbSaut = (int) Math.round(inputWav.length / saut);
        int compteur = 0;
        for (int i = 0 ; i < nbSaut ; i++){
            for (int k = 0 ; k < SEQUENCE ; k++){
                outputWav[compteur] = inputWav[(i * saut) + k];
                compteur++;
            }
        }
        return outputWav;
    }

    /**
     * Simple dilatation, with overlapping
     * @param inputWav
     * @param dilatation factor
     * @return dilated wav
     */
    public static double[] vocodeSimpleOver(double[] inputWav, double dilatation) {
        double[] outputWav = new double[(int) Math.round(inputWav.length / dilatation)];
        int saut = (int) Math.round(SEQUENCE * dilatation);
        int nbSaut = (int) Math.round(inputWav.length / saut);
        int compteur = 0;
        double k = 0.0;
        for (int i = 0 ; i < nbSaut ; i++){
            for (int j = 0 ; j < SEQUENCE ; j++){
                outputWav[compteur] = inputWav[((i * saut) + j)] * k;
                compteur++;
                if (j < 100){
                    k += 0.01;
                }
                if (j > (SEQUENCE - 100)){
                    k -= 0.01;
                }
            }
            k = 0;
        }
        return outputWav;
    }

    /**
     * Simple dilatation, with overlapping and maximum cross correlation search
     * @param inputWav
     * @param dilatation factor
     * @return dilated wav
     */
    public static double[] vocodeSimpleOverCross(double[] inputWav, double dilatation) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Play the wav
     * @param wav
     */
    public static void joue(double[] wav) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Add an echo to the wav
     * @param wav
     * @param delay in msec
     * @param gain
     * @return wav with echo
     */
    public static double[] echo(double[] input, double delayMs, double attn){
        System.out.print("cc les amies");
        double[] output = new double[input.length];
        int delay = (int) delayMs*StdAudio.SAMPLE_RATE;
        for(int i=delay;i<input.length;i++){
            output[i]+=attn*input[i-delay];
        }
        return output;
    }

    /**
     * Display the waveform
     * @param wav
     */
    public static void displayWaveform(double[] wav) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
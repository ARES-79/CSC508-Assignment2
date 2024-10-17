package Model;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataProcessor implements Runnable {

    private static final Logger dpLog = Logger.getLogger(DataProcessor.class.getName());

    private static final Emotion[] emotionValues = Emotion.values();


    public enum Emotion{
        NEUTRAL,
        FOCUS,
        STRESS,
        ENGAGEMENT,
        EXCITEMENT,
        INTEREST
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Poll with a timeout to prevent blocking indefinitely
                String eyeTrackingData = Blackboard.getInstance().pollEyeTrackingQueue();
                String emotionData = Blackboard.getInstance().pollEmotionQueue();

                if (eyeTrackingData != null && emotionData != null) {
                    // Process the pair of data
                    List<Integer> coordinates = convertToIntegerList(eyeTrackingData);
                    List<Float> emotionScores = convertToFloatList(emotionData);
                    Emotion prominentEmotion;
                    //if the emotion data is invalid, use neutral
                    if(!isValidEmotionData(emotionScores)){
                        logInvalidEmotionData(emotionData);
                        prominentEmotion = Emotion.NEUTRAL;
                    } else {
                        prominentEmotion = getProminentEmotion(emotionScores);
                    }
                    if(!isValidEyeTrackingData(coordinates)){
                        logInvalidEyeTrackingData(eyeTrackingData);
                        continue;
                    }
                    ProcessedDataObject processedData = new ProcessedDataObject(
                        coordinates.get(0),
                        coordinates.get(1),
                        prominentEmotion,
                        emotionScores
                    );

                    Blackboard.getInstance().addToProcessedDataQueue(processedData);

                    //TODO: get the prominent emotion and bundle the data together to make a circle
                } else if (eyeTrackingData != null) {
                    List<Integer> coordinates = convertToIntegerList(eyeTrackingData);
                    if(!isValidEyeTrackingData(coordinates)){
                        logInvalidEyeTrackingData(eyeTrackingData);
                        continue;
                    }
                    //TODO: bundle the coordinates with a null or blank emotion
                     ProcessedDataObject processedData = new ProcessedDataObject(
                           coordinates.get(0),
                           coordinates.get(1),
                           Emotion.NEUTRAL,
                           Arrays.asList(0.2f, 0.2f, 0.2f, 0.2f, 0.2f)
                     );
                     Blackboard.getInstance().addToProcessedDataQueue(processedData);
                } else {
                    // Handle timeout case or missing data
                    dpLog.warning("ProcessingThread: Timed out waiting for data, or one client is slow.");
                }
            }
        } catch (InterruptedException e) {
            dpLog.log(Level.SEVERE, "Thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch(Exception e){
            dpLog.warning(e.toString());
        }
    }

    private boolean isValidEyeTrackingData(List<Integer> data){
        return data != null && data.stream().allMatch(number -> number >= 0);
    }

    // Helper method to convert eye-tracking data string into integer x-y coordinates
    private List<Integer> convertToIntegerList(String data) {
        try {
            return Arrays.stream(data.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            logInvalidEyeTrackingData(data);
            return null;
        }
    }

    private void logInvalidEyeTrackingData(String data){
        dpLog.warning("Eye-tracking data must be in the form \"int, int\"\n where both are >= 0." +
                "Invalid eye-tracking data format: " + data);
    }

    private boolean isValidEmotionData(List<Float> data){
        return data != null && data.stream().allMatch(number -> number >= 0 && number <= 1);
    }

    private Color getColorFromEmotion(Emotion e){
        switch(e) {
         case NEUTRAL:
            return Color.GRAY;
         case FOCUS:
            return Color.YELLOW;
         case STRESS:
            return Color.RED;
         case ENGAGEMENT:
            return Color.BLUE;
         case EXCITEMENT:
            return Color.GREEN;
         case INTEREST:
            return Color.MAGENTA;
         default:
            // default to black
            return Color.BLACK;
         }
    }

    // Helper method to convert emotion data to list of comparable floats
    private List<Float> convertToFloatList(String data) {
        try {
            return Arrays.stream(data.split(","))
                    .map(String::trim)
                    .map(Float::parseFloat)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            logInvalidEmotionData(data);
            return null;  // Or return an empty list, or handle the error as needed
        }
    }

    public Emotion getProminentEmotion(List<Float> emotionScores){
        if (emotionScores == null || emotionScores.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }

        int maxIndex = 0;  // Assume the first element is the largest initially
        for (int i = 1; i < emotionScores.size(); i++) {
            // If current element is greater than the current max, update maxIndex
            if (emotionScores.get(i) > emotionScores.get(maxIndex)) {
                maxIndex = i;
            }
        }

        return emotionValues[maxIndex + 1];
    }

    private void logInvalidEmotionData(String data){
        dpLog.warning("Emotion data is expected to be a comma seperated list of 5 floats between 0 and 1." +
                "Invalid emotion data format: " + data);
    }

}


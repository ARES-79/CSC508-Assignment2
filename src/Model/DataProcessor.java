package Model;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The {@code DataProcessor} class processes both eye-tracking and emotion data from queues.
 * It validates, processes, and then converts this data into {@code ProcessedDataObject} instances
 * that can be used for further operations. The data includes integer coordinates for eye-tracking
 * and float-based emotion scores, with an emphasis on identifying the prominent emotion.
 * 
 * This class implements {@link Runnable} and is designed to run as a separate thread.
 * 
 * The class relies on a {@link Blackboard} to retrieve data from the input queues and add 
 * processed data objects to the output queue.
 */
public class DataProcessor implements Runnable {

    private static final Logger dpLog = Logger.getLogger(DataProcessor.class.getName());

    private static final Emotion[] emotionValues = Emotion.values();

    /**
     * Enum representing various emotions that can be processed.
     */
    public enum Emotion {
        NEUTRAL,
        FOCUS,
        STRESS,
        ENGAGEMENT,
        EXCITEMENT,
        INTEREST
    }

    /**
     * Continuously processes eye-tracking and emotion data from the {@link Blackboard}.
     * Validates the data, determines the prominent emotion, and adds the processed data
     * to the processed data queue in the Blackboard.
     * 
     * If the data is invalid or missing, the method logs appropriate warnings and defaults to neutral emotion.
     * The method runs indefinitely, periodically checking for new data and sleeping in between polling attempts.
     */
    @Override
    public void run() {
        try {
            while (true) {
                while (Blackboard.getInstance().getStartFlag()) {
                    // Poll with a timeout to prevent blocking indefinitely
                    String eyeTrackingData = Blackboard.getInstance().pollEyeTrackingQueue();
                    String emotionData = Blackboard.getInstance().pollEmotionQueue();

                    if (eyeTrackingData != null) {
                        dpLog.info("ProcessingThread: Processing data pair: " + eyeTrackingData + ", " + emotionData);
                        // Process the pair of data
                        List<Integer> coordinates = convertToIntegerList(eyeTrackingData);
                        List<Float> emotionScores = null;
                        Emotion prominentEmotion;

                        if (emotionData != null) {
                            emotionScores = convertToFloatList(emotionData);
                            // If the emotion data is invalid, default to neutral emotion
                            if (!isValidEmotionData(emotionScores)) {
                                logInvalidEmotionData(emotionData);
                                prominentEmotion = Emotion.NEUTRAL;
                            } else {
                                prominentEmotion = getProminentEmotion(emotionScores);
                            }
                        } else {
                            prominentEmotion = Emotion.NEUTRAL;
                        }

                        if (!isValidEyeTrackingData(coordinates)) {
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
                    } else if (emotionData != null) {
                        dpLog.warning("ProcessingThread: Eye-tracking data is missing, but emotion data is present.");
                    } else {
                        // Handle timeout case or missing data
                        dpLog.warning("ProcessingThread: Timed out waiting for data, or one client is slow.");
                    }
                }
                System.out.println("DataProcessor is not processing.");
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            dpLog.log(Level.SEVERE, "Thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            dpLog.warning(e.toString());
        }
    }

    /**
     * Validates that the eye-tracking data contains non-negative integers.
     * 
     * @param data the list of integers representing x-y coordinates for eye-tracking
     * @return true if all integers in the list are non-negative, false otherwise
     */
    private boolean isValidEyeTrackingData(List<Integer> data) {
        return data != null && data.stream().allMatch(number -> number >= 0);
    }

    /**
     * Converts a comma-separated string of integers (x, y coordinates) into a list of {@code Integer} objects.
     * 
     * @param data the comma-separated string of integers
     * @return a list of integers, or null if the parsing fails due to an invalid format
     */
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

    /**
     * Logs an error message when invalid eye-tracking data is encountered.
     * 
     * @param data the invalid eye-tracking data string
     */
    private void logInvalidEyeTrackingData(String data) {
        dpLog.warning("Eye-tracking data must be in the form \"int, int\"\n where both are >= 0." +
                "Invalid eye-tracking data format: " + data);
    }

    /**
     * Validates that the emotion data contains floats between 0 and 1, inclusive.
     * 
     * @param data the list of floats representing emotion scores
     * @return true if all floats are within the range [0, 1], false otherwise
     */
    private boolean isValidEmotionData(List<Float> data) {
        return data != null && data.stream().allMatch(number -> number >= 0 && number <= 1);
    }

    /**
     * Converts a comma-separated string of floats (emotion scores) into a list of {@code Float} objects.
     * 
     * @param data the comma-separated string of floats
     * @return a list of floats, or null if the parsing fails due to an invalid format
     */
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

    /**
     * Determines the prominent emotion based on the highest value in the emotion scores.
     * 
     * @param emotionScores the list of emotion scores
     * @return the most prominent emotion based on the highest score
     * @throws IllegalArgumentException if the list of emotion scores is null or empty
     */
    public Emotion getProminentEmotion(List<Float> emotionScores) {
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

    /**
     * Logs an error message when invalid emotion data is encountered.
     * 
     * @param data the invalid emotion data string
     */
    private void logInvalidEmotionData(String data) {
        dpLog.warning("Emotion data is expected to be a comma separated list of 5 floats between 0 and 1." +
                "Invalid emotion data format: " + data);
    }
}

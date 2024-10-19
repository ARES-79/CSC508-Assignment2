package Model;

import Data.Emotion;
import Data.ProcessedDataObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The {@code RawDataProcessor} class processes both eye-tracking and emotion data from queues.
 * It validates, processes, and then converts this data into {@code ProcessedDataObject} instances
 * that can be used for further operations. The data includes integer coordinates for eye-tracking
 * and float-based emotion scores, with an emphasis on identifying the prominent emotion.
 *  <p>
 * This class implements {@link Runnable} and is designed to run as a separate thread.
 *  <p>
 * The class relies on a {@link Blackboard} to retrieve data from the input queues and add
 * processed data objects to the output queue.
 */
public class RawDataProcessor extends CustomThread {

    public static final String THREAD_NAME = "DataProcessor";

    public RawDataProcessor(){
        super();
        super.setLog(Logger.getLogger(RawDataProcessor.class.getName()));
        super.setName(THREAD_NAME);
    }

    /**
     * Retrieves eye-tracking and emotion data from the {@link Blackboard} in pairs using {@link java.util.concurrent.BlockingQueue}.
     * Validates the data, determines the prominent emotion, and adds the processed data
     * to the processed data queue in the Blackboard.
     * <p>
     * If the data is invalid or missing, the method logs appropriate warnings and,
     * when necessary defaults to neutral emotion.
     */
    @Override
    public void doYourWork() throws InterruptedException, IOException {
        // Poll with a timeout to prevent blocking indefinitely
        String eyeTrackingData = Blackboard.getInstance().pollEyeTrackingQueue();
        String emotionData = Blackboard.getInstance().pollEmotionQueue();

        if (eyeTrackingData != null) {
            super.getLog().info("ProcessingThread: Processing data pair: " + eyeTrackingData + ", " + emotionData);
            // Process the pair of data
            List<Integer> coordinates = convertToIntegerList(eyeTrackingData);
            List<Float> emotionScores = null;
            Emotion prominentEmotion;
            if (emotionData != null){
                emotionScores = convertToFloatList(emotionData);
                //if the emotion data is invalid, use neutral
                if (!isValidEmotionData(emotionScores)) {
                    logInvalidEmotionData(emotionData);
                    prominentEmotion = Emotion.NONE;
                } else {
                    prominentEmotion = getProminentEmotion(emotionScores);
                }
            } else {
                prominentEmotion = Emotion.NONE;
            }

            if (!isValidEyeTrackingData(coordinates)) {
                logInvalidEyeTrackingData(eyeTrackingData);
                return; //we can't do anything without eye tracking
            }
            ProcessedDataObject processedData = new ProcessedDataObject(
                    coordinates.get(0),
                    coordinates.get(1),
                    prominentEmotion,
                    emotionScores
            );

            Blackboard.getInstance().addToProcessedDataQueue(processedData);
        }
        // debugging client/server communication
        else if (emotionData != null) {
            super.getLog().warning(THREAD_NAME + ": Eye-tracking data is missing, but emotion data is present.");
        } else {
            // Handle timeout case or missing data
            super.getLog().warning(THREAD_NAME + ": Timed out waiting for data, or one client is slow.");
        }
    }

    @Override
    public void cleanUpThread() {
        //noting needed
    }

    /**
     * Validates that the eye-tracking data contains non-negative integers.
     *
     * @param data the list of integers representing x-y coordinates for eye-tracking
     * @return true if all integers in the list are non-negative, false otherwise
     */
    private boolean isValidEyeTrackingData(List<Integer> data){
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
    private void logInvalidEyeTrackingData(String data){
        super.getLog().warning("Eye-tracking data must be in the form \"int, int\"\n where both are >= 0." +
                "Invalid eye-tracking data format: " + data);
    }

    /**
     * Validates that the emotion data contains floats between 0 and 1, inclusive.
     *
     * @param data the list of floats representing emotion scores
     * @return true if all floats are within the range [0, 1], false otherwise
     */
    private boolean isValidEmotionData(List<Float> data){
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

        return Emotion.getByValue(maxIndex);
    }

    /**
     * Logs an error message when invalid emotion data is encountered.
     *
     * @param data the invalid emotion data string
     */
    private void logInvalidEmotionData(String data){
        super.getLog().warning("Emotion data is expected to be a comma seperated list of 5 floats between 0 and 1." +
                "Invalid emotion data format: " + data);
    }

}
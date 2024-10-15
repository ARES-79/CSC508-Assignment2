package Model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataProcessor implements Runnable{

    private final BlockingQueue<String> eyeTrackingQueue;
    private final BlockingQueue<String> emotionQueue;
    private static final Logger dpLog = Logger.getLogger(DataProcessor.class.getName());

    public DataProcessor(BlockingQueue<String> eyeTrackingQueue, BlockingQueue<String> emotionQueue) {
        this.eyeTrackingQueue = eyeTrackingQueue;
        this.emotionQueue = emotionQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Poll with a timeout to prevent blocking indefinitely
                String eyeTrackingData = eyeTrackingQueue.poll(500, TimeUnit.MILLISECONDS);
                String emotionData = emotionQueue.poll(500, TimeUnit.SECONDS);

                if (eyeTrackingData != null && emotionData != null) {
                    // Process the pair of data
                    List<Integer> coordinates = convertToIntegerList(eyeTrackingData);
                    List<Float> emotionScores = convertToFloatList(emotionData);
                    //if either one of the sources has invalid data, log it and skip the pair
                    if(!isValidEyeTrackingData(coordinates)){
                        invalidEyeTrackingData(eyeTrackingData);
                        continue;
                    }
                    if(!isValidEmotionData(emotionScores)){
                        invalidEmotionData(emotionData);
                        continue;
                    }
                    //TODO: get the prominent emotion and bundle the data together to make a circle
                } else if (eyeTrackingData != null) {
                    List<Integer> coordinates = convertToIntegerList(eyeTrackingData);
                    if(!isValidEyeTrackingData(coordinates)){
                        invalidEyeTrackingData(eyeTrackingData);
                        continue;
                    }
                    //TODO: bundle the coordinates with a null or blank emotion
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
            invalidEyeTrackingData(data);
            return null;
        }
    }

    private void invalidEyeTrackingData(String data){
        dpLog.warning("Eye-tracking data must be in the form \"int, int\"\n where both are >= 0." +
                "Invalid eye-tracking data format: " + data);
    }

    private boolean isValidEmotionData(List<Float> data){
        return data != null && data.stream().allMatch(number -> number >= 0 && number <= 1);
    }

    // Helper method to convert emotion data to list of comparable floats
    private List<Float> convertToFloatList(String data) {
        try {
            return Arrays.stream(data.split(","))
                    .map(String::trim)
                    .map(Float::parseFloat)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            invalidEmotionData(data);
            return null;  // Or return an empty list, or handle the error as needed
        }
    }

    private void invalidEmotionData(String data){
        dpLog.warning("Emotion data is expected to be a comma seperated list of 5 floats between 0 and 1." +
                "Invalid emotion data format: " + data);
    }

}


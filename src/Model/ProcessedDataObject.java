package Model;

import java.util.List;

public class CircleData {

    private final List<Integer> coordinates;
    private final DataProcessor.Emotion prominentEmotion;
    private final List<Float> emotionScores;

    public CircleData(List<Integer> coordinates, DataProcessor.Emotion prominentEmotion, List<Float> emotionScores) {
        this.coordinates = coordinates;
        this.prominentEmotion = prominentEmotion;
        this.emotionScores = emotionScores;
    }

    public List<Integer> getCoordinates() {
        return coordinates;
    }

    public DataProcessor.Emotion getProminentEmotion() {
        return prominentEmotion;
    }

    public List<Float> getEmotionScores() {
        return emotionScores;
    }
}

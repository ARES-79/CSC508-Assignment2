package Model;

import java.util.List;

public record ProcessedDataObject(int xCoord, int yCoord, DataProcessor.Emotion prominentEmotion,
                                  List<Float> emotionScores) {

}

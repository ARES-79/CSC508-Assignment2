package Data;

import java.util.List;

public record ProcessedDataObject(int xCoord, int yCoord, Emotion prominentEmotion,
                                  List<Float> emotionScores) {

}

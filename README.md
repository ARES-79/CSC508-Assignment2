# CSC508-Assignment2
Version 0.1 of Project. Simple hub taking in simulated eye-tracking and emotion data

Current Idea For Processing:

The clients are set up to each add data to their own queue.
The Processor can grab from both at the same time (waiting if only one has data)
    -- the idea is that the data rates won't be too far off from each-other to cause memory issues 
    or issues with mismatching the data. 

The process can then validate the data before making a pairing of x-y co-ordinates and a primary emotion.
This new pairing can be used by the DisplayArea to draw a circle.

The DisplayArea can then just keep data about the last circle drawn, and if the next data point
from the eye tracking is within some boundary of the original point (the center of the circle),
then the last circle is updated to grow bigger and perhaps change color. the only circle to be updated will be the last one.
If the user looks at a previous circle, a new circle is formed on top of the old one. 

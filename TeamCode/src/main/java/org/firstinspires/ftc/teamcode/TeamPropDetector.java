package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class TeamPropDetector extends OpenCvPipeline {

    private static int width; // width of the image
    public static boolean isTeamPropHere;

    /**
     *
     * @param width The width of the image (check your camera)
     */
    public TeamPropDetector(int width) {
        this.width = width;
    }

    @Override
    public Mat processFrame(Mat input) {
        // "Mat" stands for matrix, which is basically the image that the detector will process
        // the input matrix is the image coming from the camera
        // the function will return a matrix to be drawn on your phone's screen

        // Make a working copy of the input matrix in HSV
        Mat mat = new Mat();
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        // if something is wrong, we assume there's no team prop
        if (mat.empty()) {
            // say that prop is not here
            return input;
        }

        // NOTE: In OpenCV's implementation, Hue values are half the real value
        // currently for blue
        // note: Hues (90 & 100) are half of actual hues (180 & 200 )
        Scalar lowHSV = new Scalar(80, 30, 40); // lower bound HSV
        Scalar highHSV = new Scalar(100, 65, 85); // higher bound HSV
        Mat thresh = new Mat();

        // gets a black & white image
        // inRange(): thresh[i][j] = {255,255,255} if mat[i][i] is within the range
        Core.inRange(mat, lowHSV, highHSV, thresh);

        // Use Canny Edge Detection to find edges
        Mat edges = new Mat();
        Imgproc.Canny(thresh, edges, 100, 300);

        // https://docs.opencv.org/3.4/da/d0c/tutorial_bounding_rects_circles.html
        // findContours connects disconnected edges.
        // We then find the bounding rectangles of those contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f[] contoursPoly  = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];
        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
        }

        // Iterate and check whether the bounding boxes cover left and/or right side of the image
        double left_x = 0.25 * width;
        double right_x = 0.75 * width;
        boolean left = false;
        boolean right = false;
        boolean middle = false;
        
        for (int i = 0; i != boundRect.length; i++) {
            /*
            if (boundRect[i].x < left_x)
                left = true;
            if (boundRect[i].x + boundRect[i].width > right_x)
                right = true;
           */
            if (boundRect[i].x + boundRect[i].width < right_x && boundRect[i].x + boundRect[i].width > left_x)
                middle = true;
            // draw red bounding rectangles on mat
            // the mat has been converted to HSV so we need to use HSV as well
            Imgproc.rectangle(mat, boundRect[i], new Scalar(0.5, 76.9, 89.8));
        }

/*        if (left) location = TeamPropLocation.LEFT;
        else if (right) location = TeamPropLocation.RIGHT;
        else location = TeamPropLocation.NONE;
        */
        // in theory, this should detect if the team prop is in the middle
        if (middle) {
            isTeamPropHere = true;
        } else {
            isTeamPropHere = false;
        }
        return mat; // return the mat with rectangles drawn
    }

    public boolean getLocation() {
        return this.isTeamPropHere;
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import vision.Webcam;
// import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;


@Autonomous
public class blueClose extends OpMode {
    //@Override
// This below here is the code for the actual claw
// driveOmni(-1*gamepad1.left_stick_y, 1*gamepad1.right_stick_x, 1*gamepad1.left_stick_x);
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    // down there makes a webcam
//    private Webcam webcam;
    double power = 1;
    int step = 0;
    int delayStep = -1;
    double endTime;
    public ElapsedTime timer = new ElapsedTime();

    public void delayedStop(double delay) {
        if (delayStep != step) {
            delayStep = step;
            endTime = timer.milliseconds() + delay;

        }
        if (timer.milliseconds() >= endTime) {
            stopRobot();
            step++;
        }
        //stops
    }

    public void driveWithTime(double y, double rx, double x, double time) {
        switch (step) {
            case (0):
                driveOmni(y, rx, x);
                delayedStop(time);
                break;
            case (1):
                stopRobot();
                break;
        }
    }


    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
//        this.webcam = new Webcam(this.hardwareMap, "Webcam");
//        this.webcam.open(new ColorCapturePipeline());
//        private AbstractResultCvPipeline<?> pipeline;
        // note: I don't actually know the width or height, should probably check this.
        int width = 320;
        int height = 240;
        TeamPropDetector detector = new TeamPropDetector(width);
        OpenCvCamera phoneCam;
    }

    public void driveOmni(double y, double rx, double x) {
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double flPower = (x + y + rx) / maxValue;
        double blPower = (x - y + rx) / maxValue;
        double frPower = (x - y - rx) / maxValue;
        double brPower = (x + y - rx) / maxValue;

        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(blPower);
        backRight.setPower(brPower);
    }

    public void stopRobot() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void placePurplePixel() {
        switch (step) {
            case (0):
                driveOmni(0.5, 00, 0);
                delayedStop(1500);
                break;
            case (1):
                driveOmni(-0.5, 0, -0);
                delayedStop(1500);
                break;
        }
    }

    @Override
    public void loop() {
        int width = 320;
        int height = 240;
        OpenCvCamera camera;
        TeamPropDetector detector = new TeamPropDetector(width);
        // Initialize the camera
//        this.webcam = new Webcam(this.hardwareMap, "Webcam");
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.FRONT);

        // Connect to the camera
        camera.openCameraDevice();
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }

        });

        // Use the TeamPropDetector pipeline
        // processFrame() will be called to process the frame
        camera.setPipeline(detector);

        //...

        TeamPropDetector.isTeamPropHere = detector.getLocation();
        if (TeamPropDetector.isTeamPropHere) {
            placePurplePixel();
        } else {
            driveWithTime(0, 0, 0.5, 1000);
            // check for team prop in front of robot
            TeamPropDetector.isTeamPropHere = detector.getLocation();
            if (TeamPropDetector.isTeamPropHere) {
                placePurplePixel();
            } else {
                // we assume it's in this third position, if it isn't in the other two.
                driveWithTime(0, 0, -0.5, 2000);
                placePurplePixel();
            }

            switch (step) {
                case (0):
                    driveOmni(0, 00, -0.5);
                    delayedStop(2400);
                    break;
            }
        }
    }

}

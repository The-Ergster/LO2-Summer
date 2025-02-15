package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import vision.Webcam;
// import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Autonomous
public class AutoDelayedStop extends OpMode {
    //@Override

    // wheel motors
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;

    // other motors / servos
    private DcMotor intake;
    private DcMotor slides;
    private DcMotor arm;
    private CRServo bucket;

    int slidesTime;
    int bucketTime;

    int step = 0;
    int delayStep = -1;
    double endTime;
    public ElapsedTime timer = new ElapsedTime();

    public void delayedStop(double delay){
        if (delayStep!=step){
            delayStep=step;
            endTime=timer.milliseconds()+delay;

        }
        if (timer.milliseconds()>=endTime) {
            stopRobot();
            step++;
        }
        //stops
    }

    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");

        intake = hardwareMap.get(DcMotor.class, "in");
        arm = hardwareMap.get(DcMotor.class, "ar");
        slides = hardwareMap.get(DcMotor.class, "sl");
        bucket = hardwareMap.get(CRServo.class, "bu");

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//        this.webcam = new Webcam(this.hardwareMap, "Webcam");
//        this.webcam.open(new ColorCapturePipeline());
//        private AbstractResultCvPipeline<?> pipeline;
    }
    public void driveOmni(double y, double rx, double x) {
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double flPower = (y + x + rx) / maxValue;
        double blPower = (y - x + rx) / maxValue;
        double frPower = (y - x - rx) / maxValue;
        double brPower = (y + x - rx) / maxValue;

        frontLeft.setPower(-flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(-blPower);
        backRight.setPower(brPower);


    }

    public void stopRobot(){
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    @Override
    public void loop() {

        slidesTime = 3000;
        bucketTime = 2000;

        switch (step){
            case (0):
                // move forward
                driveOmni(0.5,00,0);
                delayedStop(2000);
                break;
            case (1):
                // rotate
                driveOmni(0,0.5,0.5);
                delayedStop(1000);
                break;
            case (2):
                // slides up
                slides.setPower(-0.2);
                delayedStop(slidesTime);
                break;
            case (3):
                // stop slides
                slides.setPower(0);
                // dump the bucket
                bucket.setPower(0.1);
                delayedStop(bucketTime);
                break;
            case (4):
                // return the bucket to its place
                bucket.setPower(-0.1);
                delayedStop(bucketTime);
                break;
            case (5):
                // slides back down
                slides.setPower(.5);
                delayedStop(slidesTime);
                break;
            case (6):
                // move arm down
                //-- MAKE THIS MOVE ARM
                break;
            case (7):
                // move robot forward to first block
                // FIX THESE VALUES!!!!
                driveOmni(100,0,100);
                delayedStop(1000);
                break;
            case (8):
                // stop the robot
                stopRobot();
                // intake block
                intake.setPower(0.5);
                delayedStop(1000);
                break;
            case (9):
                // stop moving intake
                intake.setPower(0);
                // put arm back up
                //-- MAKE THIS MOVE THE ARM
                break;
            case (10):
                // that's it for now, whoops!
                break;
            case (11):


        }

    }
}
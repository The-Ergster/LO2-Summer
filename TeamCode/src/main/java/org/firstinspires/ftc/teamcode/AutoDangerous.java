package org.firstinspires.ftc.teamcode;

import static android.os.SystemClock.sleep;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class AutoDangerous extends OpMode{





    //defines motors back left, back right,
        private DcMotor frontLeft;
        private DcMotor frontRight;
        private DcMotor backRight;
        private DcMotor backLeft;
        private DcMotor intake;
        private DcMotor slides;
        private DcMotor arm;
        private CRServo bucket;
        public ElapsedTime timer = new ElapsedTime();
        double time = 0;
        double ticks = 537.7;
        int test = 0;
        int funnyTime = 0;
        int startTime = 0;
        int currentStep = 0;


        //puts motor names into phone language

        public void init() {
            timer = new ElapsedTime();

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
            slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        //takes motor power and translates into joystick movements
        public void driveOmni(double y, double rx, double x){
            double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double flPower = (y + x + rx) / maxValue;
            double blPower = (y - x + rx) / maxValue;
            double frPower = (y - x - rx) / maxValue;
            double brPower = (y + x - rx) / maxValue;

//sets motor power combined with joystick equal to phone language

            frontLeft.setPower(-flPower);
            frontRight.setPower(frPower);
            backLeft.setPower(-blPower);
            backRight.setPower(brPower);


        }

   /* public void driveRaw(double flPower, double frPower, double brPower, double blPower){
        //fl, fr, br, bl
        double[] motorCoefficients = {1,1,1,1};
        frontLeft.setPower(motorCoefficients[0]*flPower);
        frontRight.setPower(motorCoefficients[1]*frPower);
        backRight.setPower(motorCoefficients[2]*brPower);
        backLeft.setPower(motorCoefficients[3]*blPower);
    }
   */


    public void outputNextStep() {
        // shows the current step
        telemetry.addData("Current step: ", currentStep);
        telemetry.update();
        currentStep++;

    }


//takes entire thing and makes it work cause yeah :)

        public double[] calculateMotorPowerGivenCartesianPoint(double deltaY, double deltaX) {
//        double[][] cartesianToWheelMatrix = {{-Math.sqrt(2)/4, Math.sqrt(2)/4}, {-Math.sqrt(2)/4, -Math.sqrt(2)/4}};
//        double[] distance = {0.0, 0.0};
            double[] wheelCoordsFrBr = {(Math.sqrt(2)*deltaX+Math.sqrt(2)*deltaY)/2.0,(-Math.sqrt(2)*deltaX+Math.sqrt(2)*deltaY)/2.0};
            double[] answer = {wheelCoordsFrBr[0], wheelCoordsFrBr[1], wheelCoordsFrBr[0], wheelCoordsFrBr[1]};
            // answer is in fr br bl fl
            double cmToTick = 537.7/30.16;
            double errorCorrection = 1.10;
            double[] answerTicks = {answer[0]*cmToTick, answer[1]*cmToTick*errorCorrection, answer[2]*cmToTick, answer[3]*cmToTick*errorCorrection};
            return answerTicks;
        }

        public double[] calculateEncoderPositionGivenDegrees(double deg){
            double fudgeFactor = 1;
            double [] answerTicks = {fudgeFactor*deg*15.75/10*537.7/90.0, 0, fudgeFactor*deg*15.75/10*537.7/90.0, 0};
            return answerTicks;
        }

        public void stopRobot(){
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }

        public void timerToZero(){
            // resets the timer to zero


        }

        public void setMotorTargets(double[] answerTicks) {
            frontLeft.setTargetPosition(-(int)answerTicks[0]);
            frontRight.setTargetPosition(-(int)answerTicks[1]);
            backLeft.setTargetPosition(-(int)answerTicks[2]);
            backRight.setTargetPosition(-(int)answerTicks[3]);
        }
        public void setMotorTargets2(double[] answerTicks) {
            frontLeft.setTargetPosition((int)answerTicks[0]);
            frontRight.setTargetPosition(-(int)answerTicks[1]);
            backLeft.setTargetPosition((int)answerTicks[2]);
            backRight.setTargetPosition(-(int)answerTicks[3]);
        }

        public void setsStrafeTargets(double[] answerTicks, String direction){
            if(direction.equals("ri")){
                frontLeft.setTargetPosition((int)answerTicks[0]);
                frontRight.setTargetPosition(-(int)answerTicks[1]);
                backLeft.setTargetPosition(-(int)answerTicks[2]);
                backRight.setTargetPosition((int)answerTicks[3]);
            } else if (direction.equals("le")) {
                frontLeft.setTargetPosition(-(int)answerTicks[0]);
                frontRight.setTargetPosition((int)answerTicks[1]);
                backLeft.setTargetPosition((int)answerTicks[2]);
                backRight.setTargetPosition(-(int)answerTicks[3]);
            }

        }

        public void transferToSlides(){

        }

        public void encoder(int turnage){
            double newTarget = ticks/turnage;
            frontLeft.setTargetPosition((int)turnage);
            frontRight.setTargetPosition(-(int)turnage);
            backLeft.setTargetPosition((int)turnage);
            backRight.setTargetPosition(-(int)turnage);

            frontLeft.setPower(0.3);
            frontRight.setPower(-0.3);
            backLeft.setPower(0.3);
            backRight.setPower(-0.3);

            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        }


        public void start() {


            startTime = (int) timer.milliseconds();


            slides.setTargetPosition(-2900);
            slides.setPower(0.3);
            slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // move the intake
            intake.setTargetPosition(-238);
            intake.setPower(0.3);
            intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //this is supposed to rotate 90 degrees

            //setMotorTargets(calculateEncoderPositionGivenDegrees(-90.0));

            //Move slides up all the way

        }


        public void loop() {

            int startTime = (int) timer.milliseconds(); // Get current time
            int duration = 2000; // Set desired duration in milliseconds
            currentStep = 1; // Sets step to 0 to be played

/*            while (System.currentTimeMillis() < startTime + duration) {
                bucket.setPower(0.5); // Set power to 0.5 for clockwise rotation
            } */

            // step 1: wait for 2 seconds
            outputNextStep();
            while (startTime + duration > timer.milliseconds()){
                continue;
            }

            // step 2: move forward
            outputNextStep();
            setMotorTargets(calculateMotorPowerGivenCartesianPoint(80, 0));
            while (frontLeft.isBusy()) {
                continue;
            }

            // step 3: Rotate left
            outputNextStep();
            while (timer.milliseconds() - startTime < 2000) {
                driveOmni(0 ,-.5 ,0 );
            }

            // step 4: put up the slides
            outputNextStep();
            slides.setTargetPosition(-70);
            slides.setPower(0.7);
            slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while (slides.isBusy()) {
                continue;
            }

            // step 5: rotate the servo
            outputNextStep();
            bucket.setPower(0.1);
            while (slides.isBusy()) {
                continue;
            }

            // step 6: stop the bucket
            bucket.setPower(0); // Stop the servo



        }


        //\\ public void loop() {

        //funnyTime = (float) timer.milliseconds();
        // while (timer.milliseconds() - funnyTime < 500) {
        //  driveOmni(1,0,0);
        //}

/*        // Move forward
        while (timer.milliseconds() - funnyTime < 2400) {
            driveOmni(0.1, 0, 0);
        }

        // Rotate left
        while (timer.milliseconds() - funnyTime >1000 && timer.milliseconds() - funnyTime <2000) {
            driveOmni(0 ,-5 ,0 );
        }

        //
        while (funnyTime != 0) {
            stopRobot();
        }
        */


        //}

    }

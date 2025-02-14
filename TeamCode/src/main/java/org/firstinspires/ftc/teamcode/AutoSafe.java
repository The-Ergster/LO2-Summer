//robotics code that does things
package org.firstinspires.ftc.teamcode;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;


//defines motors back left, back right,
@Autonomous
public class AutoSafe extends OpMode {
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


    //puts motor names into phone language
    @Override
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

    @Override

    public void start() {

        // move the intake
        intake.setTargetPosition(-238);
        intake.setPower(0.3);
        intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    //this is supposed to rotate 90 degrees

    //

        //setMotorTargets(calculateEncoderPositionGivenDegrees(-90.0));

//Move slides up all the way



    }

    @Override
    public void loop() {

    }

    // public void loop() {

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

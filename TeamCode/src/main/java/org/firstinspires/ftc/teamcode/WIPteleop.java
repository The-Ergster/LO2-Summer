//robotics code that does things
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


//defines motors back left, back right,

@TeleOp
public class WIPteleop extends OpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor intake;
    private DcMotor slides;
    private CRServo bucket;
    private DcMotor arm; // rotating arm with intake
    public ElapsedTime timer = new ElapsedTime();
    float funnyTime = 0;
    double armPosition;
    boolean slidesUp;
    boolean armUp;




    //puts motor names into phone language
    @Override
    public void init() {
        int encoderMin = 0;
        int encoderMid = 60;
        int encoderMax = 227;
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        intake = hardwareMap.get(DcMotor.class, "in");
        slides = hardwareMap.get(DcMotor.class, "sl");
        arm = hardwareMap.get(DcMotor.class, "ar");
        bucket = hardwareMap.get(CRServo.class, "bu");

        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intake.setTargetPosition(0);
        intake.setPower(0.1);
        intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slides.setTargetPosition(210);
        slides.setPower(0.5);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
//takes motor power and translates into joystick movements
    public void driveOmni(double y, double rx, double x){
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double flPower = (y + x + rx) / maxValue;
        double blPower = (y - x + rx) / maxValue;
        double frPower = (y - x - rx) / maxValue;
        double brPower = (y + x - rx) / maxValue;


//sets motor power combined with joystick equal to phone language

        frontLeft.setPower(flPower);
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
   /* Specific commands */
    public void flipArm() {
        funnyTime = (float) timer.milliseconds();
        while (timer.milliseconds()<=1000+funnyTime) {
            arm.setPower(.3);
        }
        arm.setPower(0);
        while (timer.milliseconds()>=1000+funnyTime && timer.milliseconds()<=2000){
            intake.setPower(-.15);
        }
        intake.setPower(0);
    }
/*
    public void armControl(boolean armUp, boolean armDown) {
        //armUp = false;
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double armPower;
        if (armUp == true) {
            armPower = .5;
            armPosition++;
        } else if (armDown == true) {
            armPower = -.5;
            armPosition--;
        } else {
            armPower = 0;
        }

        intake.setPower(armPower);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
*/

    public void armControl(boolean fakeIntakeUp,boolean fakeIntakeDown ){

        if (fakeIntakeUp && fakeIntakeDown) {

            intake.setTargetPosition(60);
            intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        } else if(fakeIntakeDown) {

            intake.setTargetPosition(30);
            intake.setPower(-0.3);
            intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        } else if (fakeIntakeUp) {
            intake.setTargetPosition(190);
            intake.setPower(0.3);
            intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        } else {
            intake.setPower(0);
            intake.setTargetPosition(intake.getCurrentPosition());
        }
    }

// takes entire thing and makes it work cause yeah :)
    public void loop() {
        int cp = intake.getCurrentPosition();
        telemetry.addData("slides encoder", slides.getCurrentPosition());
        telemetry.addData("arm encoder", intake.getCurrentPosition());
        telemetry.addData("bucket encoder", bucket.getPower());
        driveOmni(-1*gamepad1.left_stick_y, 1*gamepad1.right_stick_x, 1*gamepad1.left_stick_x);

// actually arm

//        if(!intake.isBusy()){
//            intake.setPower(0);
//        }


        // this is for the arm.
        armControl(gamepad1.a,gamepad1.b);


/*        if (gamepad1.a) {
            armUp = false;
        } else if (gamepad1.b) {
            armUp = true;
        }
        if (armUp){
            intake.setTargetPosition(100);
        } else {
            intake.setTargetPosition(0);
        } */

        //armControl(gamepad1.a, gamepad1.b);

        //if(gamepad1.a && cp)


        if (gamepad1.y) {
            slidesUp = true;
        } else if (gamepad1.x) {
            slidesUp = false;
        }



        if (slidesUp) {
            slides.setTargetPosition(2900);
            slides.setPower(1.0);
        } else {
            slides.setTargetPosition(50);
            slides.setPower(-0.7);
        }

        if (gamepad1.right_bumper){
            bucket.setPower(1.0);
        } else if (gamepad1.left_bumper) {
            bucket.setPower(-1.0);
        } else {
            bucket.setPower(0);
        }

// actually intake
        if (gamepad1.left_trigger>0.8){
            arm.setPower(1);
            telemetry.addData("power set to 1","arm");
            telemetry.update();
        } else if (gamepad1.right_trigger>0.8){
            arm.setPower(-1);
            telemetry.addData("power set to -1","arm");
            telemetry.update();
        } else {
            arm.setPower(0);
            telemetry.addData("power set to 0","arm");
            telemetry.update();
        }
        telemetry.addData("EncoderArm: ", intake.getCurrentPosition());
        telemetry.update();

    }
}    

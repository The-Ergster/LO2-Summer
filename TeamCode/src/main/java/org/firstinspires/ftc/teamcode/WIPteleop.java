//robotics code that does things
package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;


//defines motors back left, back right,
@Config
//Config is needed for the PID, same as the public static variables
@TeleOp
public class WIPteleop extends OpMode {
    private PIDController controller;

    public static double p =0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;


    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor intake;
    private DcMotor slides;
    private CRServo bucket;
    private DcMotorEx arm; // rotating arm with intake
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

        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        arm = hardwareMap.get(DcMotorEx.class, "in");
        slides = hardwareMap.get(DcMotor.class, "sl");
        intake = hardwareMap.get(DcMotor.class, "ar");
        bucket = hardwareMap.get(CRServo.class, "bu");

        slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm.setTargetPosition(0);
        arm.setPower(0.1);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slides.setTargetPosition(-500);
        slides.setPower(0.7);
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

            arm.setTargetPosition(60);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        } else if(fakeIntakeDown) {

            arm.setTargetPosition(30);
            arm.setPower(-0.3);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        } else if (fakeIntakeUp) {
            arm.setTargetPosition(190);
            arm.setPower(0.3);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        } else {
            arm.setPower(0);
            arm.setTargetPosition(arm.getCurrentPosition());
        }
    }

// takes entire thing and makes it work cause yeah :)
    public void loop() {
        // to use all the pid stuff, go to 192.168.49.1:8080/dash on the phone while connected to the robot
        // if you can't get this working next time, just comment out all the pid stuff
        // more explanation: "PIDF Loops & Arm Control | FTC | 16379 KookyBotz" youtube video
        // btw the pid is all for the arm, so if you get the FTC dashboard working, move the arm and then the values will change and then adjust until the arm stays
        controller.setPID(p, i, d);
        int armPos = arm.getCurrentPosition();
        double pid = controller.calculate(armPos, target);
        double ff = Math.cos(target) * f;

        double armPIDPower = pid * ff;

        int cp = arm.getCurrentPosition();
        telemetry.addData("slides encoder", slides.getCurrentPosition());
        telemetry.addData("arm encoder", armPos);
        telemetry.addData("arm target", target);
        telemetry.addData("bucket encoder", bucket.getPower());
        driveOmni(-1*gamepad1.left_stick_y, 1*gamepad1.right_stick_x, 1*gamepad1.left_stick_x);

// actually arm

//        if(!intake.isBusy()){
//            intake.setPower(0);
//        }


        // this is for the arm.
        armControl(gamepad1.a,gamepad1.b);

/*
       if (gamepad1.a) {
            armUp = false;
        } else if (gamepad1.b) {
            armUp = true;
        }
        if (armUp){
            intake.setTargetPosition(100);
        } else {
            intake.setTargetPosition(0);
        }
*/

        //armControl(gamepad1.a, gamepad1.b);

        //if(gamepad1.a && cp)


       if (gamepad1.y) {
            slidesUp = true;
        } else if (gamepad1.x) {
            slidesUp = false;
        }

        if (slidesUp) {
            slides.setTargetPosition(-2900);
            slides.setPower(1.0);
            slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        } else if(!slidesUp){
            slides.setTargetPosition(-500);
            slides.setPower(0.7);
            slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
            intake.setPower(1);
            telemetry.addData("power set to 1","intake");
            telemetry.update();
        } else if (gamepad1.right_trigger>0.8){
            intake.setPower(-1);
            telemetry.addData("power set to -1","intake");
            telemetry.update();
        } else {
            intake.setPower(0);
            telemetry.addData("power set to 0","intake");
            telemetry.update();
        }
        telemetry.addData("EncoderArm: ", arm.getCurrentPosition());
        telemetry.update();

    }
}    

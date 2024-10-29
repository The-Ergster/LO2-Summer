//robotics code that does things
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

//Boo
// defines motors back left, back right,
@TeleOp
public class TeleopMode extends OpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor intake;
    private DcMotor arm; // rotating arm with intake
    private DcMotor bucket;
    private DcMotor slides;

    double armPower = 0.2; // this is a test number
    double bucketPower = 0.1; // this is also a test number
    double intakePower = 0.1; // this is also a test number
    double slidePower = 0.2;

//I like cheese
    int step = 0;
    int delayStep = -1;
    double endTime;
    public ElapsedTime timer = new ElapsedTime();

    public void delayedStop(double delay){
        if (delayStep != step){
            delayStep = step;
            endTime = timer.milliseconds()+delay;

        }
        if (timer.milliseconds()>=endTime) {
            step++;
        }
        //stops
    }


//I eat cheese

    //puts motor names into phone language
    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        intake = hardwareMap.get(DcMotor.class, "in");
        arm = hardwareMap.get(DcMotor.class, "ar");
        bucket = hardwareMap.get(DcMotor.class, "bu");
        slides = hardwareMap.get(DcMotor.class, "sl");

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
 /*   public void flipArm() {

        arm.setPower(armPower);
         //if(){ //slidesDown will be a boolean about slide position, we will make it later
            step = 1;
         // }

        switch(step) {
            case(0):
                // make sure slide bucket is lowered [only runs if slides need to move down]
                slides.setPower(-.3);
                delayedStop(1000);
                // set slidesDown to true
                break;
            case(1):
                // move arm to back
                arm.setPower(-1 * armPower);
                delayedStop(1000);
                break;
            case(2):
                // drop sample
                intake.setPower(-1 * intakePower);
                delayedStop(1000);
                break;
            case(3):
                // flip arm back
                arm.setPower(armPower);
                delayedStop(1000);
                break;

        }

    } */

    public void backwardsFlipArm() {
        arm.setPower(armPower);
         //if(){ //slidesDown will be a boolean about slide position, we will make it later
            step = 1;
         // }

        switch(step) {
            case(0):
                // make sure slide bucket is lowered [only runs if slides need to move down]
                slides.setPower(-.3);
                delayedStop(1000);
                // set slidesDown to true
                break;
            case(1):
                // move arm to back
                slides.setPower(0);
                arm.setPower(-1 * armPower);
                delayedStop(1000);
                break;
            case(2):
                // drop sample
                arm.setPower(0);
                intake.setPower(-1 * intakePower);
                delayedStop(1000);
                break;
            case(3):
                intake.setPower(0);

        }

    }



    public void forwardsFlipArm() {

        arm.setPower(armPower);
         //if(){ //slidesDown will be a boolean about slide position, we will make it later
            step = 1;
         // }

        switch(step) {
            case(0):
                // flip arm back
                arm.setPower(armPower);
                delayedStop(1000);
                break;
            case(1):
                arm.setPower(0);
                break;
        }

    }



// takes entire thing and makes it work cause yeah :)

    public void loop() {
        driveOmni(-1*gamepad1.left_stick_y, 1*gamepad1.right_stick_x, 1*gamepad1.left_stick_x);

        if (gamepad1.a){
            intake.setPower(.15);
        } else if (gamepad1.b) {
            intake.setPower(-.15);
        } else {
            intake.setPower(0);
        }


        if (gamepad1.x){
            backwardsFlipArm();
        } else if (gamepad1.y){
            forwardsFlipArm();
        }


        if (gamepad1.dpad_up) {
            slides.setPower(slidePower);
        } else if (gamepad1.dpad_down) {
            slides.setPower(-slidePower);
        }


    }

}    

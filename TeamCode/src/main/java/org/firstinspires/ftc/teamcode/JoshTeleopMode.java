//robotics code that does things
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


//defines motors back left, back right,
@TeleOp
public class JoshTeleopMode extends OpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor intake;
    private DcMotor arm; // rotating arm with intake
    public ElapsedTime timer = new ElapsedTime();
    float funnyTime = 0;




    //puts motor names into phone language
    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        intake = hardwareMap.get(DcMotor.class, "in");
        arm = hardwareMap.get(DcMotor.class, "ar");

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


//takes entire thing and makes it work cause yeah :)

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
            while(gamepad1.x) { continue; }
            flipArm();
        }

    }
}    

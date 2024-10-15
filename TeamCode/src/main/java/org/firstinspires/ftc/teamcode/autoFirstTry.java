//robotics code that does things
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;


//defines motors back left, back right,
@Autonomous
public class autoFirstTry extends OpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor intake;
    private DcMotor arm;
    public ElapsedTime timer = new ElapsedTime();


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


//takes entire thing and makes it work cause yeah :)

    public void stopRobot(){
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void transferToSlides(){

    }

    public void loop() {
        if (timer.milliseconds()<1000 ) {
            driveOmni(0, 0, -.5);
        } else if (timer.milliseconds()>1000 && timer.milliseconds()<2000){
            driveOmni(0 ,-5 ,0 );
        } else {
            stopRobot();
        }
    }

}
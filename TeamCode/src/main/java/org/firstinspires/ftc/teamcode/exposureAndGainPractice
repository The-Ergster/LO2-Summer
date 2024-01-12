package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TeleopMode extends OpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    //now new code
    private DcMotor armMotor;

    private double armPosition;
    private CRServo clawServo;


    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        armMotor = hardwareMap.get(DcMotor.class, "am");
//        clawServo = hardwareMap.get(CRServo.class, "cs");
        armPosition = 0;
    }

    public void driveOmni(double y, double rx, double x){
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double flPower = (y + x + rx) / maxValue;
        double blPower = (y - x + rx) / maxValue;
        double frPower = (y - x - rx) / maxValue;
        double brPower = (y + x - rx) / maxValue;

        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(blPower);
        backRight.setPower(brPower);
    }
    /*
    public void driveRaw(double flPower, double frPower, double brPower, double blPower){
        //fl, fr, br, bl
        double[] motorCoefficients = {1,1,1,1};
        frontLeft.setPower(motorCoefficients[0]*flPower);
        frontRight.setPower(motorCoefficients[1]*frPower);
        backRight.setPower(motorCoefficients[2]*brPower);
        backLeft.setPower(motorCoefficients[3]*blPower);
    }
     */
    public void armControl(boolean armUp, boolean armDown) {
        //armUp = false;
        double armPower;
        if (armUp == true && armPosition <= 13) {
            armPower = .3;
            armPosition++;
        } else if (armDown == true && armPosition >= 0) {
            armPower = -.3;
            armPosition--;
        } else {
            armPower = 0;
        }

        armMotor.setPower(armPower);
    }
/*    public void clawControl(boolean clawOpen, boolean clawClose) {
        clawClose = false;
        double clawPower;
        if (clawClose == true) {
            clawPower = 1;
        } else if (clawOpen == true) {
            clawPower = -1;
        } else {
            clawPower = 0;
        }

        clawServo.setPower(clawPower);
    }
    */

    @Override
    public void loop() {
        driveOmni(-1*gamepad1.left_stick_y, 1*gamepad1.right_stick_x, 1*gamepad1.left_stick_x);
        armControl(gamepad1.left_bumper, gamepad1.right_bumper);
        // clawControl(gamepad1.a, gamepad1.b);
    }
}

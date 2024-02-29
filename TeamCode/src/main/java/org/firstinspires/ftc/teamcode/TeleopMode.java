package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

@TeleOp
public class TeleopMode extends OpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    //now new code
    private DcMotor armMotor;
    private double armPosition = 100;
//    private CRServo droneLauncher;
public TelemetryImpl telemetry;



    @Override
    public void init() {
        telemetry = new TelemetryImpl(this);
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        armMotor = hardwareMap.get(DcMotor.class, "am");
//        droneLauncher = hardwareMap.get(CRServo.class, "cs");
        armPosition = 0;
    }

    public void driveOmni(double y, double rx, double x){
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        // note: fl motor is wonky and therefore y backwards
        double flPower = (-y - x + rx) / maxValue;
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
        if (armUp == true) {
            armPower = 1;
            armPosition++;
        } else if (armDown == true) {
            armPower = -.8;
            armPosition--;
        } else {
            armPower = 0;
        }

        armMotor.setPower(armPower);
    }

    /*
    public void droneControl(boolean clawOpen, boolean clawClose) {
        clawClose = false;
        double clawPower;
        if (clawClose == true) {
            clawPower = 1;
        } else if (clawOpen == true) {
            clawPower = -1;
        } else {
            clawPower = 0;
        }

        droneLauncher.setPower(clawPower);

}
 */
    
    @Override
    public void loop() {
        if (gamepad1.y){
            driveOmni(0.1,0,0);
            telemetry.addLine("y");
        } else if (gamepad1.x) {
              driveOmni(0,0,-0.1);
            telemetry.addLine("x");
          } else if (gamepad1.b) {
              driveOmni(0, 0, 0.1);
              telemetry.addLine("b");
          } else if(gamepad1.a) {
              driveOmni(-0.1,0,0);
            telemetry.addLine("a");
          } else {
            driveOmni(-1*gamepad1.left_stick_y, 1*gamepad1.right_stick_x, 1*gamepad1.left_stick_x);
            telemetry.addLine("y: -1*" + gamepad1.left_stick_y);
            telemetry.addLine("rx: " + gamepad1.right_stick_x);
            telemetry.addLine("x: "+gamepad1.left_stick_x);
            telemetry.addLine("moving");
          }
          telemetry.update();
//        armControl(gamepad1.left_bumper, gamepad1.right_bumper);
//        clawControl(gamepad1.a, gamepad1.b);
    }
}
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class TeleopRecorder extends OpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    public TelemetryImpl telemetry;
    public ElapsedTime timer = new ElapsedTime();
    double endTime;
    int fStep = 1;
    int bStep = 1;
    int lStep = 1;
    int rStep = 1;

    int delayStep = -1;
    int miniStep = 1;
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
    public void stopDrive(){
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
    public void driveRaw(double flPower, double frPower, double brPower, double blPower){
        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backRight.setPower(brPower);
        backLeft.setPower(blPower);
    }
    
    
    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
    }
    

    @Override

    public void loop() {
        if (gamepad1.dpad_up) {
            driveRaw(0.5f,0.5f,0.5f,0.5f);
            switch(fStep) {
                case(1):
                    timer.reset();
                    fStep++;
                    break;
                case(2):
                    telemetry.addData("Forward time: ", timer.milliseconds());
            }
        } else if (gamepad1.dpad_left) {
            driveOmni(0,0,-0.5);
            switch(lStep) {
                case(1):
                    timer.reset();
                    lStep++;
                    break;
                case(2):
                    telemetry.addData("Left time: ", timer.milliseconds());
            }
        } else if (gamepad1.dpad_down) {
            driveRaw(-0.5,-0.5,-0.5,-0.5);
            switch(bStep) {
                case(1):
                    timer.reset();
                    bStep++;
                    break;
                case(2):
                    telemetry.addData("Backward time: ", timer.milliseconds());
            }
        } else if (gamepad1.dpad_right) {
            driveOmni(0, 0, 0.5);
            switch(rStep) {
                case(1):
                    timer.reset();
                    rStep++;
                    break;
                case(2):
                    telemetry.addData("Right Time: ", timer.milliseconds());
            }
        } else {
            stopDrive();
            fStep = 1;
            bStep = 1;
            rStep = 1;
            lStep = 1;
        }
        telemetry.update();

    }
    public void stop() {
        stopDrive();
    }
}

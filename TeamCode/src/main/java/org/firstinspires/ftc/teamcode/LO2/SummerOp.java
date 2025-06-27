package org.firstinspires.ftc.teamcode.LO2;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class SummerOp extends OpMode {
    //creates motor class
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;

    @Override
    public void init() {
        //defines motors
        frontLeft = hardwareMap.get(DcMotorEx.class, "fl");
        frontRight = hardwareMap.get(DcMotorEx.class, "fr");
        backLeft = hardwareMap.get(DcMotorEx.class, "bl");
        backRight = hardwareMap.get(DcMotorEx.class, "br");

        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setDirection(DcMotorEx.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);

        frontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Initiated 1.0");
        telemetry.update();
    }

    public void driveOmni(double y, double rx, double x) {
        //braking
        final double MAX_TICKS_PER_SECOND = 4661;


        if (gamepad1.left_stick_y <= 0.05 && gamepad1.right_stick_x <= 0 && gamepad1.left_stick_x <= 0) {
            // Stop all motors
            frontLeft.setVelocity(0);
            backLeft.setVelocity(0);
            frontRight.setVelocity(0);
            backRight.setVelocity(0);
            return;
        }
        //math stuff for movement
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double flPower = (y + x + rx) / maxValue;
        double blPower = (y - x + rx) / maxValue;
        double frPower = (y - x - rx) / maxValue;
        double brPower = (y + x - rx) / maxValue;

        frontLeft.setVelocity(flPower * MAX_TICKS_PER_SECOND);
        backLeft.setVelocity(blPower * MAX_TICKS_PER_SECOND);
        frontRight.setVelocity(frPower * MAX_TICKS_PER_SECOND);
        backRight.setVelocity(brPower * MAX_TICKS_PER_SECOND);
    }

    @Override
    public void start() {
        telemetry.addLine("Go go go!!!!!");
        telemetry.update();
    }

    @Override
    public void stop() {
        telemetry.addLine("Stopped");
        telemetry.update();
    }

    @Override
    public void loop() {
        //Fun fact, this one line is what drives everything
        //actual code for movement
        driveOmni(-1*gamepad1.left_stick_y, 1*gamepad1.right_stick_x, 1*gamepad1.left_stick_x);


    }
}




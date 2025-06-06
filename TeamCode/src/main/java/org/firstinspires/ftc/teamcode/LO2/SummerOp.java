package org.firstinspires.ftc.teamcode.LO2;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class SummerOp extends OpMode {
    String gamepad1Buttons = "";
    //Create gamepad 1 telemetry
    String gamepad2Buttons = "";
    //Create gamepad 2 telemetry
    private DcMotorEx frontLeft;
    //create motor variable with Ex typing
    private DcMotorEx frontRight;
    private DcMotorEx backRight;
    private DcMotorEx backLeft;

    @Override
    public void init() {
        //defines motors for configuration (put "fr" or something else in the config)
        frontLeft = hardwareMap.get(DcMotorEx.class, "fl");
        frontRight = hardwareMap.get(DcMotorEx.class, "fr");
        backLeft = hardwareMap.get(DcMotorEx.class, "bl");
        backRight = hardwareMap.get(DcMotorEx.class, "br");
        //Direction defined
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        //Encoder initiation
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Initiated");
        telemetry.update();
    }

    public void driveOmni(double y, double rx, double x) {
        //Math stuff stolen from the internet
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double flPower = (y + x + rx) / maxValue;
        double blPower = (y - x + rx) / maxValue;
        double frPower = (y - x - rx) / maxValue;
        double brPower = (y + x - rx) / maxValue;

        final double MAX_TICKS_PER_SECOND = 2300; // change based on motor type I know we use a goBilda something

        frontLeft.setVelocity(flPower * MAX_TICKS_PER_SECOND);
        backLeft.setVelocity(blPower * MAX_TICKS_PER_SECOND);
        frontRight.setVelocity(frPower * MAX_TICKS_PER_SECOND);
        backRight.setVelocity(brPower * MAX_TICKS_PER_SECOND);
    }

    //When play is pressed
    public void start() {
            telemetry.addLine("Go go go!!!!!");
            telemetry.update();
        }
    //When stop is pressed
    public void stop(){
       telemetry.addLine("Stopped");
       telemetry.update();
    }

    public void loop() {
        //Fun fact, this one line is what drives everything
        driveOmni(-1*gamepad1.left_stick_y, 1*gamepad1.right_stick_x, 1*gamepad1.left_stick_x);

        //Telemetry for all buttons
        if (gamepad1.a) gamepad1Buttons  = "1A ";
        else if (gamepad1.b) gamepad1Buttons  = "1B ";
        else if (gamepad1.x) gamepad1Buttons  = "1X ";
        else if (gamepad1.y) gamepad1Buttons  = "1Y ";
        else if (gamepad1.left_bumper) gamepad1Buttons  = "1LB ";
        else if (gamepad1.right_bumper) gamepad1Buttons  = "1RB ";
        else if (gamepad1.dpad_down) gamepad1Buttons  = "1Dpad Down ";
        else if (gamepad1.dpad_up) gamepad1Buttons  = "1Dpad Up ";
        else if (gamepad1.dpad_left) gamepad1Buttons  = "1Dpad Left ";
        else if (gamepad1.dpad_right) gamepad1Buttons  = "1Dpad Right ";
        else if (gamepad1.left_stick_x > 0) gamepad1Buttons  = "1Left Stick ";
        else if (gamepad1.left_stick_y > 0) gamepad1Buttons  = "1Left Stick ";
        else if (gamepad1.right_stick_x > 0) gamepad1Buttons  = "1Right Stick ";
        else if (gamepad1.right_stick_y > 0) gamepad1Buttons  = "1Right Stick ";
        else if (gamepad1.start) gamepad1Buttons  = "1Start ";
        else if (gamepad1.back) gamepad1Buttons  = "1Back ";
        else if (gamepad1.left_trigger > 0) gamepad1Buttons  = "1Left Trigger ";
        else if (gamepad1.right_trigger > 0) gamepad1Buttons  = "1Right Trigger ";
        else gamepad1Buttons="";


        telemetry.addData("Gamepad 1", gamepad1Buttons);


        if (gamepad2.a) gamepad2Buttons  = "2A ";
        else if (gamepad2.b) gamepad2Buttons  = "2B ";
        else if (gamepad2.x) gamepad2Buttons  = "2X ";
        else if (gamepad2.y) gamepad2Buttons  = "2Y ";
        else if (gamepad2.left_bumper) gamepad2Buttons  = "2LB ";
        else if (gamepad2.right_bumper) gamepad2Buttons  = "2RB ";
        else if (gamepad2.dpad_down) gamepad2Buttons  = "2Dpad Down ";
        else if (gamepad2.dpad_up) gamepad2Buttons  = "2Dpad Up ";
        else if (gamepad2.dpad_left) gamepad2Buttons  = "2Dpad Left ";
        else if (gamepad2.dpad_right) gamepad2Buttons  = "2Dpad Right ";
        else if (gamepad2.left_stick_x > 0) gamepad2Buttons  = "2Left Stick ";
        else if (gamepad2.left_stick_y > 0) gamepad2Buttons  = "2Left Stick ";
        else if (gamepad2.right_stick_x > 0) gamepad2Buttons  = "2Right Stick ";
        else if (gamepad2.right_stick_y > 0) gamepad2Buttons  = "2Right Stick ";
        else if (gamepad2.start) gamepad2Buttons  = "2Start ";
        else if (gamepad2.back) gamepad2Buttons  = "2Back ";
        else if (gamepad2.left_trigger > 0) gamepad2Buttons  = "2Left Trigger ";
        else if (gamepad2.right_trigger > 0) gamepad2Buttons  = "2Right Trigger ";
        else gamepad1Buttons="";

        telemetry.addData("Gamepad 2", gamepad2Buttons);

        telemetry.update();
    }
}

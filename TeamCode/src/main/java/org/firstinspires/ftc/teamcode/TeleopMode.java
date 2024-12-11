package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class TeleopMode extends OpMode {

    // Declare motors and other hardware components
    private DcMotor frontLeft, frontRight, backLeft, backRight, intake, arm, bucket, slides;
    private double armPower = 0.2, bucketPower = 0.1, intakePower = 0.1, slidePower = 0.2;
    private int step = 0, delayStep = -1;
    private double endTime;
    private ElapsedTime timer = new ElapsedTime();

    // Initialization of hardware
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

    // Omni drive logic
    public void driveOmni(double y, double rx, double x) {
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double flPower = (y + x + rx) / maxValue;
        double blPower = (y - x + rx) / maxValue;
        double frPower = (y - x - rx) / maxValue;
        double brPower = (y + x - rx) / maxValue;

        frontLeft.setPower(-flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(-blPower);
        backRight.setPower(brPower);
    }

    // Control Intake motor
    private void controlIntake() {
        if (gamepad1.a) {
            intake.setPower(0.15); // Intake in
        } else if (gamepad1.b) {
            intake.setPower(-0.15); // Intake out
        } else {
            intake.setPower(0); // Stop intake
        }
    }

    // Control Arm motion
    private void controlArm() {
        if (gamepad1.x) {
            flipArm(true); // Flip arm backwards
        } else if (gamepad1.y) {
            flipArm(false); // Flip arm forwards
        }
    }

    // Flip Arm logic (Backwards or Forwards)
    private void flipArm(boolean isBackwards) {
        arm.setPower(isBackwards ? -armPower : armPower);

        // Set delay logic if necessary
        delayedStop(1000);
    }

    // Slide control
    private void controlSlides() {
        if (gamepad1.dpad_up) {
            slides.setPower(slidePower); // Move slides up
        } else if (gamepad1.dpad_down) {
            slides.setPower(-slidePower); // Move slides down
        } else {
            slides.setPower(0); // Stop slides
        }
    }

    // Delayed stop logic (general purpose)
    public void delayedStop(double delay) {
        if (delayStep != step) {
            delayStep = step;
            endTime = timer.milliseconds() + delay;
        }
        if (timer.milliseconds() >= endTime) {
            step++; // Move to next step after delay
        }
    }

    // Main loop for controlling robot
    @Override
    public void loop() {
        // Drive control
        driveOmni(-gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

        // Intake control
        controlIntake();

        // Arm control (forwards/backwards flip)
        controlArm();

        // Slide control (up/down)
        controlSlides();

        // Telemetry for debugging
        telemetry.addData("Arm Power", arm.getPower());
        telemetry.addData("Intake Power", intake.getPower());
        telemetry.addData("Slide Power", slides.getPower());
        telemetry.update();
    }
}

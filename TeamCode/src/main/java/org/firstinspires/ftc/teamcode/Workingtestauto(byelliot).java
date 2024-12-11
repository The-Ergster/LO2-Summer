package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Encoder;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class DragRaceAuto extends OpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor linearSlideMotor;
    private DcMotor armMotor;

    private ElapsedTime timer = new ElapsedTime();
    private int step = 0;

    // Motor encoder target values
    private static final int DRIVE_DISTANCE_INCHES = 60; // 5 feet = 60 inches
    private static final int TURN_DEGREES = 45;

    // Initialize motors
    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        linearSlideMotor = hardwareMap.get(DcMotor.class, "linearSlide");
        armMotor = hardwareMap.get(DcMotor.class, "arm");

        // Reset motors to ensure proper operation
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        linearSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set motors to run with encoder feedback
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Omni drive function
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

    // Function to stop the robot
    public void stopRobot() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    // Move robot a certain distance (e.g., 5 feet)
    public void moveDistance(double power, int targetInches) {
        int targetCounts = targetInches * 45; // Convert inches to encoder counts (assuming 45 encoder counts per inch)
        frontLeft.setTargetPosition(targetCounts);
        frontRight.setTargetPosition(targetCounts);
        backLeft.setTargetPosition(targetCounts);
        backRight.setTargetPosition(targetCounts);

        // Run to position
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    // Turn robot by certain degrees (e.g., 45 degrees)
    public void turnByDegrees(double power, int degrees) {
        // Assuming 45 encoder counts per degree, adjust as needed
        int turnCounts = degrees * 45;

        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + turnCounts);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - turnCounts);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + turnCounts);
        backRight.setTargetPosition(backRight.getCurrentPosition() - turnCounts);

        // Run to position
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    // Move linear slide motor to the target position
    public void moveLinearSlide(double power, int targetPosition) {
        linearSlideMotor.setTargetPosition(targetPosition);
        linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideMotor.setPower(power);
    }

    // Move arm motor to target position
    public void moveArm(double power, int targetPosition) {
        armMotor.setTargetPosition(targetPosition);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(power);
    }

    @Override
    public void loop() {

        switch (step) {
            case 0:
                // Drive left for 5 feet
                moveDistance(0.5, DRIVE_DISTANCE_INCHES);
                step++;
                break;

            case 1:
                // Wait until the robot has reached the target position
                if (!frontLeft.isBusy() && !frontRight.isBusy()) {
                    stopRobot();
                    step++;
                }
                break;

            case 2:
                // Turn 45 degrees to the right
                turnByDegrees(0.35, TURN_DEGREES);
                step++;
                break;

            case 3:
                // Wait until the turn is complete
                if (!frontLeft.isBusy() && !frontRight.isBusy()) {
                    stopRobot();
                    step++;
                }
                break;

            case 4:
                // Raise the linear slide
                moveLinearSlide(1.0, 1000); // Move to position 1000 (example)
                step++;
                break;

            case 5:
                // Wait until the linear slide is fully raised
                if (!linearSlideMotor.isBusy()) {
                    linearSlideMotor.setPower(0);
                    step++;
                }
                break;

            case 6:
                // Move the arm to the other side
                moveArm(1.0, 1000); // Move to position 1000 (example)
                step++;
                break;

            case 7:
                // Wait until the arm reaches the target position
                if (!armMotor.isBusy()) {
                    armMotor.setPower(0);
                    step++;
                }
                break;

            case 8:
                // End of autonomous, stop everything
                stopRobot();
                break;
        }

        telemetry.addData("Step", step);
        telemetry.update();
    }
}

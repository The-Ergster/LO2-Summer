package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        linearSlideMotor = hardwareMap.get(DcMotor.class, "linearSlide");
        armMotor = hardwareMap.get(DcMotor.class, "arm");

        // Reset motors to ensure proper operation
        linearSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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

    @Override
    public void loop() {

        switch (step) {
            case 0:
                // Drive left for 5 feet (assuming 1 ft = 12 inches, 5 feet = 60 inches)
                // Adjust speed and duration based on your robot's drive system.
                driveOmni(0, 0, -0.5);  // Negative X for leftward motion
                timer.reset();
                step++;
                break;

            case 1:
                // Wait until the robot has driven for 5 feet.
                if (timer.seconds() >= 2) {  // Adjust this duration based on testing
                    stopRobot();
                    step++;
                }
                break;

            case 2:
                // Turn 45 degrees to the right
                driveOmni(0, 0.35, 0);  // Rotate by positive rx (turn to the right)
                timer.reset();
                step++;
                break;

            case 3:
                // Wait for the 45-degree turn to complete
                if (timer.seconds() >= 1) {  // Adjust based on robot turn speed
                    stopRobot();
                    step++;
                }
                break;

            case 4:
                // Raise the linear slide (assuming a motor-based linear slide)
                linearSlideMotor.setPower(1.0);  // Raise the slide
                timer.reset();
                step++;
                break;

            case 5:
                // Wait for the linear slide to fully raise (adjust duration based on slide speed)
                if (timer.seconds() >= 2) {
                    linearSlideMotor.setPower(0);  // Stop the linear slide motor
                    step++;
                }
                break;

            case 6:
                // Move the arm fully to the other side (assuming the arm motor is for this)
                armMotor.setPower(1.0);  // Move the arm
                timer.reset();
                step++;
                break;

            case 7:
                // Wait for the arm to reach the other side (adjust duration based on arm speed)
                if (timer.seconds() >= 2) {
                    armMotor.setPower(0);  // Stop the arm motor
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

    private int step = 0;
}

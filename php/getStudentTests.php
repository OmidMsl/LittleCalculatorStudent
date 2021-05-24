<?php

require_once 'config.php';
// get parameters
if (isset($_REQUEST['student_id'])){
    $sid = $_REQUEST['student_id'];
} else{
    return;
}

// connect to database
$con = new mysqli(DB_HOST , DB_USER , DB_PASS , DB_NAME);

// check connection
if ($con->connect_error) {
    echo "Failed to connect : " . $con->connect_error;
    return;
}

$content = mysqli_query($con , "SELECT * FROM `Test` WHERE `student_id`=$sid ORDER BY `date` DESC");
$output = array();

while($row = mysqli_fetch_array($content)){
    $record  = array();
    $record['id'] = $row['id'];
    $record['num_of_all_questions'] = $row['num_of_all_questions'];
    $record['num_of_correct_answers'] = $row['num_of_correct_answers'];
    $record['is_mul'] = $row['is_mul'];
    $record['num_of_digits'] = $row['num_of_digits'];
    $record['date'] = $row['date'];
    $output[] = $record;
}

echo json_encode($output);

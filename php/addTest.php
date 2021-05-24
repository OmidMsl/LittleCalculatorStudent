<?php

require_once 'config.php';

// get parameters
if(isset($_REQUEST['student_id']) && isset($_REQUEST['num_of_all_questions']) &&
    isset($_REQUEST['num_of_correct_answers']) && isset($_REQUEST['is_mul']) &&
    isset($_REQUEST['num_of_digits']) && isset($_REQUEST['date'])) {

    $student_id = $_REQUEST['student_id'];
    $num_of_all_questions = $_REQUEST['num_of_all_questions'];
    $num_of_correct_answers = $_REQUEST['num_of_correct_answers'];
    $is_mul = $_REQUEST['is_mul'];
    $num_of_digits = $_REQUEST['num_of_digits'];
    $date = $_REQUEST['date'];
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

$sqlquery = "INSERT INTO `Test` (`student_id`, `num_of_all_questions`, `num_of_correct_answers`,".
    " `is_mul`, `num_of_digits`, `date`) VALUES ($student_id, $num_of_all_questions,".
    " $num_of_correct_answers, $is_mul, $num_of_digits, $date)";

if ($con->query($sqlquery) === true){
    echo "New record created successfully";
} else{
    echo "Error : " . $con->error;
}

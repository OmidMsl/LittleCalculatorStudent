<?php

require_once 'config.php';
// get parameters
if (isset($_REQUEST['id'])){
    $id = $_REQUEST['id'];
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

$content = mysqli_query($con , "SELECT * FROM `Message` WHERE `sender_id`=$id OR `receiver_id`=$id ORDER BY `date`");
$output = array();

while($row = mysqli_fetch_array($content)){
    $record  = array();
    $record['id'] = $row['id'];
    $record['content'] = $row['content'];
    $record['sender_id'] = $row['sender_id'];
    $record['receiver_id'] = $row['receiver_id'];
    $record['is_from_teacher'] = $row['is_from_teacher'];
    $record['is_observed'] = $row['is_observed'];
    $record['date'] = $row['date'];
    $output[] = $record;
}

echo json_encode($output);

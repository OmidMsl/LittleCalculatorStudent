<?php

require_once 'config.php';

// get parameters
if(isset($_REQUEST['content']) && isset($_REQUEST['sender_id']) && isset($_REQUEST['receiver_id'])
    && isset($_REQUEST['is_from_teacher']) && isset($_REQUEST['is_observed'])
    && isset($_REQUEST['date'])) {

    $content = $_REQUEST['content'];
    $sender_id = $_REQUEST['sender_id'];
    $receiver_id = $_REQUEST['receiver_id'];
    $is_from_teacher = $_REQUEST['is_from_teacher'];
    $is_observed = $_REQUEST['is_observed'];
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
mysqli_query( $con , "SET CHARACTER SET utf8;" );

$sqlquery = "INSERT INTO `Message` (`content`, `sender_id`, `receiver_id`, `is_from_teacher`,".
    " `is_observed`, `date`) VALUES ('$content', $sender_id, $receiver_id, $is_from_teacher,".
    " $is_observed, $date)";

if ($con->query($sqlquery) === true){
    $content = mysqli_query($con , "SELECT * FROM `Message` WHERE `sender_id`=$sender_id OR `receiver_id`=$sender_id ORDER BY `date`");
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
} else{
    echo "Error : " . $con->error;
}

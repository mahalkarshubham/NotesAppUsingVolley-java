    <?php
    require '../connect.inc.php';
    require '../core.inc.php';

    $response = array();

   @header('Content-Type: application/json; charset=utf-8'); 

    if(
        isset($_POST['note_id']) &&
        isset($_POST['title']) &&
        isset($_POST['note']) &&
        isset($_POST['date_time']))
    {
        $note_id = @$_POST['note_id'];
        $title = @$_POST['title'];
        $note = @$_POST['note'];
        $date_time = @$_POST['date_time'];
       
        if(
                !empty($note_id) &&
                !empty($title) &&
                !empty($note) &&
                !empty($date_time)){

            $status = 'Active';
            $time_stamp = date('Y-m-d');
                
                $query = "UPDATE `notes` SET
                `title`=:title,
                `note`=:note,
                `date_time`=:date_time,
                `status`=:status,
                `time_stamp`=:time_stamp WHERE
                `note_id`=:$note_id";
                
                 $statment = $connect->prepare($query);
                 $result = $statment->execute(
                array(
                        ":note_id" => $note_id,
                        ":title" => $title,
                        ":note" => $note,
                        ":date_time" => $date_time,
                        ":status" => "Active",
                        ":time_stamp" => $time_stamp
                        ));
                
                        if($result){
                                $response [0]['status'] = "Active";
                                $response [0]['message'] = "Profile updated successfully";
                                echo json_encode($response);
                        }else{
                                $response [0]['status'] = "Failed";
                                $response [0]['message'] = "Failed to update profile, try again";
                                $error = $statment->errorInfo();
                                $response [0]['error'] = $error[2];
                                echo json_encode($response);
                        }
            
    }else{
            $response [0]['status'] = "Failed";
            $response [0]['message'] = "* is mandatory";
            echo json_encode($response);
    }
    }else{
            $response [0]['status'] = "Failed";
            $response [0]['message'] = "Some parameters are missing, try again";
            
            echo json_encode($response);
}
?>
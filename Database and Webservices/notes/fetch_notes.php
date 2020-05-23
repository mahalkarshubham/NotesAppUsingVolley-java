<?php
require 'connect.inc.php';

    $response = array();

   @header('Content-Type: application/json; charset=utf-8'); 

        $query = "SELECT * FROM notes WHERE status = 'Active'";
        $statment = $connect->query($query);
        
       if($statment){
                    $result = $statment->fetchAll(PDO::FETCH_ASSOC);
                        if(count($result) == 0){
                            $response [0]['status'] = "Failed";
                            $response [0]['message'] = "No Notes found";
                            echo json_encode($response);
            
        }else if(count($result))
    {              
        echo json_encode($result);
                        
       }else{
        $response [0]['status'] = "Failed";
         $response [0]['message'] = "No Notes found";
            echo json_encode($response);
       }
                      
       }else{
            $response [0]['status'] = "Failed";
         $response [0]['message'] = "Failed to load notes";
           echo json_encode($response);
       }

?>

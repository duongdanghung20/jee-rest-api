import { useState } from "react";
import { useHistory, useParams } from "react-router-dom";

const UpdateAssignment = () => {

    const {assignmentId, assignmentTeacherId} = useParams();
    const [teacherId, setTeacherId] = useState(assignmentTeacherId);
    const [isPending, setIsPending] = useState(false);
    const history = useHistory();

    const handleSubmit = (e) => {
        e.preventDefault();
        const assignment = {'teacherId': teacherId};

        setIsPending(true);

        let formBody = [];
        for (let property in assignment) {
            let encodedKey = encodeURIComponent(property);
            let encodedValue = encodeURIComponent(assignment[property]);
            formBody.push(encodedKey + "=" + encodedValue);
        }
        formBody = formBody.join("&");

        fetch(`http://localhost:8080/gdd/${assignmentId}`, {
            method: 'PUT',
            headers: { 'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8' },
            body: formBody
        })
        .then(() => {
            alert(`Updated assignment ${assignmentId} successfully!`);
            setIsPending(false);
            history.goBack();
        })
    }

    return (
        <div className="add">
            <h1>Update Assignment</h1>
            <form onSubmit={handleSubmit}>
                <label>Teacher ID:</label>
                <input 
                    type="number" 
                    required 
                    value={teacherId} 
                    onChange={(e) => setTeacherId(e.target.value)}/>
                { !isPending && <button onClick={handleSubmit}>Add</button> }
                { isPending && <button disabled>Adding...</button> }
            </form>
        </div>
    );
}
 
export default UpdateAssignment;
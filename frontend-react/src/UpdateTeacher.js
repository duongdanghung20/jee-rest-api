import { useState } from "react";
import { useHistory, useParams } from "react-router-dom";

const UpdateTeacher = () => {

    const {teacherId, teacherFirstName, teacherLastName, teacherEq, teacherDept, teacherService, teacherNumDisHours, teacherMaxOvtHours} = useParams();
    const [firstName, setFirstName] = useState(teacherFirstName);
    const [lastName, setLastName] = useState(teacherLastName);
    const [eq, setEq] = useState(teacherEq);
    const [dept, setDept] = useState(teacherDept);
    const [service, setService] = useState(teacherService);
    const [numDisHours, setNumDisHours] = useState(teacherNumDisHours);
    const [maxOvtHours, setMaxOvtHours] = useState(teacherMaxOvtHours);
    const [isPending, setIsPending] = useState(false);
    const history = useHistory();

    const handleSubmit = (e) => {
        e.preventDefault();
        const teacher = { 'firstName': firstName, 'lastName': lastName, 'eq': eq, 'dept': dept, 'service': service, 'numDisHours': numDisHours, 'maxOvtHours': maxOvtHours };

        setIsPending(true);

        let formBody = [];
        for (let property in teacher) {
            let encodedKey = encodeURIComponent(property);
            let encodedValue = encodeURIComponent(teacher[property]);
            formBody.push(encodedKey + "=" + encodedValue);
        }
        formBody = formBody.join("&");

        fetch(`http://localhost:8080/rh/${teacherId}`, {
            method: 'PUT',
            headers: { 'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8' },
            body: formBody
        })
        .then(() => {
            alert(`Updated teacher ${teacherId} successfully!`);
            setIsPending(false);
            history.goBack();
        })
    }

    return (
        <div className="add">
            <h1>Update teacher</h1>
            <form onSubmit={handleSubmit}>
                <label>First Name:</label>
                <input 
                    type="text" 
                    required 
                    value={firstName} 
                    onChange={(e) => setFirstName(e.target.value)}/>
                <label>Last Name:</label>
                <input 
                    type="text" 
                    required 
                    value={lastName} 
                    onChange={(e) => setLastName(e.target.value)}/>
                <label>TP:TD Equivalent:</label>
                <select 
                    type="number"
                    required 
                    value={eq} 
                    onChange={(e) => {setEq(e.target.value)}}>
                    <option value={1}>1</option>
                    <option value={2/3}>2:3</option>
                </select>
                <label>Department:</label>
                <input 
                    type="text" 
                    required 
                    value={dept} 
                    onChange={(e) => setDept(e.target.value)}/>
                <label>Service:</label>
                <select 
                    type="text"
                    required 
                    value={service} 
                    onChange={(e) => {setService(e.target.value)}}>
                    <option value="Enseignant">Enseignant</option>
                    <option value="Doctorant">Doctorant</option>
                    <option value="Enseignant-chercheur">Enseignant-chercheur</option>
                </select>
               <label>Number of discharged hours:</label>
                <select 
                    type="number"
                    required 
                    value={numDisHours} 
                    onChange={(e) => {setNumDisHours(e.target.value)}}>
                    <option value={0}>0</option>
                    <option value={32}>32</option>
                    <option value={64}>64</option>
                    <option value={128}>128</option>
                </select>
                {(service === "Doctorant"
                            ? (null)
                            : (<label>Maximum number of overtime hours:</label>))}
                {(service === "Doctorant"
                            ? (null)
                            : (<input 
                                type="number" 
                                required 
                                value={maxOvtHours} 
                                onChange={(e) => setMaxOvtHours(e.target.value)}/>))}
                { !isPending && <button onClick={handleSubmit}>Update</button> }
                { isPending && <button disabled>Updating...</button> }
            </form>
        </div>
    );
}
 
export default UpdateTeacher;
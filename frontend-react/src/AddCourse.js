import { useState } from "react";
import { useHistory } from "react-router-dom";

const AddCourse = () => {
    const [name, setName] = useState('');
    const [semester, setSemester] = useState(1);
    const [numCMHours, setNumCMHours] = useState(null);
    const [numTDHours, setNumTDHours] = useState(null);
    const [numTPHours, setNumTPHours] = useState(null);
    const [numStudents, setNumStudents] = useState(null);
    const [thresholdCM, setThresholdCM] = useState(null);
    const [thresholdTD, setThresholdTD] = useState(null);
    const [thresholdTP, setThresholdTP] = useState(null);

    const [isPending, setIsPending] = useState(false);
    const history = useHistory();

    const handleSubmit = (e) => {
        e.preventDefault();
        const course = { 'name': name, 'semester': semester, 'numCMHours': numCMHours, 'numTDHours': numTDHours, 'numTPHours': numTPHours, 'numStudents': numStudents, 'thresholdCM': thresholdCM, 'thresholdTD': thresholdTD, 'thresholdTP': thresholdTP };

        setIsPending(true);

        let formBody = [];
        for (let property in course) {
            let encodedKey = encodeURIComponent(property);
            let encodedValue = encodeURIComponent(course[property]);
            formBody.push(encodedKey + "=" + encodedValue);
        }
        formBody = formBody.join("&");

        fetch('http://localhost:8080/scolarite/', {
            method: 'POST',
            headers: { 'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8' },
            body: formBody
        })
        .then(() => {
            alert('Added new course successfully!');
            setIsPending(false);
            history.goBack();
        })
    }

    return (
        <div className="add">
            <h1>Add new course</h1>
            <form onSubmit={handleSubmit}>
                <label>Name:</label>
                <input 
                    type="text" 
                    required 
                    value={name} 
                    onChange={(e) => setName(e.target.value)} 
                    placeholder='Course name'/>
                <label>Semester:</label>
                <select 
                    type="number"
                    required 
                    value={semester} 
                    onChange={(e) => {setSemester(e.target.value)}}>
                    <option value={1}>1</option>
                    <option value={2}>2</option>
                    <option value={3}>3</option>
                    <option value={4}>4</option>
                </select>
                <label>Number of CM hours:</label>
                <input 
                    type="number" 
                    required 
                    value={numCMHours} 
                    onChange={(e) => setNumCMHours(e.target.value)}/>
                <label>Number of TD hours:</label>
                <input 
                    type="number" 
                    required 
                    value={numTDHours} 
                    onChange={(e) => setNumTDHours(e.target.value)}/>
                <label>Number of TP hours:</label>
                <input 
                    type="number" 
                    required 
                    value={numTPHours} 
                    onChange={(e) => setNumTPHours(e.target.value)}/>
                <label>Number of students:</label>
                <input 
                    type="number" 
                    required 
                    value={numStudents} 
                    onChange={(e) => setNumStudents(e.target.value)}/>
                <label>Threshold for CM groups:</label>
                <input 
                    type="number" 
                    required 
                    value={thresholdCM} 
                    onChange={(e) => setThresholdCM(e.target.value)}/>
                <label>Threshold for TD groups:</label>
                <input 
                    type="number" 
                    required 
                    value={thresholdTD} 
                    onChange={(e) => setThresholdTD(e.target.value)}/>
                <label>Threshold for TP groups:</label>
                <input 
                    type="number" 
                    required 
                    value={thresholdTP} 
                    onChange={(e) => setThresholdTP(e.target.value)}/>
                { !isPending && <button onClick={handleSubmit}>Add</button> }
                { isPending && <button disabled>Adding...</button> }
            </form>
        </div>
    );
}
 
export default AddCourse;
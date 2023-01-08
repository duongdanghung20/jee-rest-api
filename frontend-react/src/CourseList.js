import { ReactComponent as DeleteBtn } from './imgs/delete.svg';
import { ReactComponent as EditBtn } from './imgs/edit.svg';
import { useHistory } from 'react-router-dom';

const CourseList = ({courses}) => {
    const colNames = ["ID", "Name", "Semester", "Num Hours", "Num Students", "Threshold", "Num Groups", "Action"];
    const history = useHistory();

    const handleDelete = (courseId) => {
        fetch(`http://localhost:8080/scolarite/${courseId}`, {
            method: 'DELETE'
        }).then(() => {
            alert(`Deleted course with ID ${courseId}!`);
            history.go(0);
        })
    }

    const handleEdit = (courseId, name, semester, numCMHours, numTDHours, numTPHours, numStudents, thresholdCM, thresholdTD, thresholdTP, numCMGroups, numTDGroups, numTPGroups) => {
        history.push(`/update/${courseId}/${name}/${semester}/${numCMHours}/${numTDHours}/${numTPHours}/${numStudents}/${thresholdCM}/${thresholdTD}/${thresholdTP}/${numCMGroups}/${numTDGroups}/${numTPGroups}`);
    }

    return (
        <div className="course-list">
            <table className="course-table" style={{borderCollapse: 'collapse'}}>
                <tbody>
                    <tr>
                        {colNames.map((colname) => (
                            <th id={colname.toLowerCase().replace(' ', '')} key={colname}>{colname}</th>
                        ))}
                    </tr>
                    {courses.map((course) => (
                        <tr key={course.id}>
                            <td className='idcol'>{course.id}</td>
                            <td className='namecol'>{course.name}</td>
                            <td className='semestercol'>{course.semester}</td>
                            <td className='numhourscol'>
                                <div>CM: {course.numCMHours}</div>
                                <div>TD: {course.numTDHours}</div>
                                <div>TP: {course.numTPHours}</div>                            
                            </td>
                            <td className='numstudentscol'>{course.numStudents}</td>
                            <td className='thresholdcol'>
                                <div>CM: {course.thresholdCM}</div>
                                <div>TD: {course.thresholdTD}</div>
                                <div>TP: {course.thresholdTP}</div>                            
                            </td>
                            <td className='numgroupscol'>
                                <div>CM: {course.numCMGroups}</div>
                                <div>TD: {course.numTDGroups}</div>
                                <div>TP: {course.numTPGroups}</div>                            
                            </td>
                            <td className='actioncol'>
                                <div className="editbtndiv">
                                    <button className='editbtn'><EditBtn onClick={() => handleEdit(course.id, course.name, course.semester, course.numCMHours, course.numTDHours, course.numTPHours, course.numStudents, course.thresholdCM, course.thresholdTD, course.thresholdTP, course.numCMGroups, course.numTDGroups, course.numTPGroups)}/></button>
                                </div>
                                <div className="deletebtndiv">
                                    <button className='deletebtn'><DeleteBtn onClick={() => handleDelete(course.id)}/></button>
                                </div>
                                
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
 
export default CourseList;
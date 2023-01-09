import { ReactComponent as EditBtn } from './imgs/edit.svg';
import { useHistory } from 'react-router-dom';


const AssignmentList = ({assignments, courses}) => {
    const colNames = ["ID", "Course", "Teacher ID", "Group Type", "Group Number", "Num Hours", "Action"];
    const history = useHistory();

    const handleEdit = (assignmentId, teacherId) => {
        history.push(`/update/${assignmentId}/${teacherId}`);
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
                    {assignments.map((assignment) => (
                        <tr key={assignment.id}>
                            <td className='idcol'>{assignment.id}</td>
                            <td className='coursenamecol'>{courses.map((course) => (course.id === assignment.courseId) && course.name)}</td>
                            <td className='teacheridcol'>{assignment.teacherId === -1 ? "Unassigned" : assignment.teacherId}</td>
                            <td className='grouptypecol'>{assignment.groupType}</td>
                            <td className='groupnumcol'>{assignment.groupNumber}</td>
                            <td className='numhourscol'>{assignment.numHours}</td>
                            <td className='actioncol'>
                                <div className="editbtndiv">
                                    <button className='editbtn'><EditBtn onClick={() => handleEdit(assignment.id, assignment.teacherId)}/></button>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
 
export default AssignmentList;
import { ReactComponent as DeleteBtn } from './imgs/delete.svg';
import { ReactComponent as EditBtn } from './imgs/edit.svg';
import { useHistory } from 'react-router-dom';

const TeacherList = ({teachers}) => {
    const colNames = ["ID", "First Name", "Last Name", "Num Hours", "Eq", "Dept", "Service", "DHours", "Ovt Hours", "Action"];
    const history = useHistory();

    const handleDelete = (teacherId) => {
        fetch(`http://localhost:8080/rh/${teacherId}`, {
            method: 'DELETE'
        }).then(() => {
            alert(`Deleted teacher with ID ${teacherId}!`);
            history.go(0);
        })
    }

    const handleEdit = (teacherId, firstName, lastName, numHours, eq, dept, service, numDisHours, maxOvtHours) => {
        history.push(`/update/${teacherId}/${firstName}/${lastName}/${numHours}/${eq}/${dept}/${service}/${numDisHours}/${maxOvtHours}`);
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
                    {teachers.map((teacher) => (
                        <tr key={teacher.id}>
                            <td className='idcol'>{teacher.id}</td>
                            <td className='firstnamecol'>{teacher.firstName}</td>
                            <td className='lastnamecol'>{teacher.lastName}</td>
                            <td className='numhourscol'>{teacher.numHours}</td>
                            {(teacher.eq !== 1
                            ? (<td className='eqcol'> 2:3 </td>)
                            : (<td className='eqcol'>{teacher.eq}</td>))}
                            <td className='deptcol'>{teacher.dept}</td>
                            <td className='servicecol'>{teacher.service}</td>
                            <td className='dhourscol'>{teacher.numDisHours}</td>
                            <td className='ovthourscol'>{teacher.maxOvtHours}</td>
                            <td className='actioncol'>
                                <div className="editbtndiv">
                                    <button className='editbtn'><EditBtn onClick={() => handleEdit(teacher.id, teacher.firstName, teacher.lastName, teacher.numHours, teacher.eq, teacher.dept, teacher.service, teacher.numDisHours, teacher.maxOvtHours)}/></button>
                                </div>
                                <div className="deletebtndiv">
                                    <button className='deletebtn'><DeleteBtn onClick={() => handleDelete(teacher.id)}/></button>
                                </div>
                                
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
 
export default TeacherList;
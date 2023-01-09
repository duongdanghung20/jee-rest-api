const AssignmentSearchCourse = ({assignments}) => {

    let courseTeachers = [];
    let courseAssignments = {};

    let courseId = assignments[0].courseId;


    for (let assignment of assignments) {
        if (!(courseTeachers.includes(assignment.teacherId)) && assignment.teacherId !== -1) {
            courseTeachers.push(assignment.teacherId);
            courseAssignments[assignment.teacherId] = 0;
        }
    }

    for (let assignment of assignments) {
        if (assignment.teacherId !== -1) {
            courseAssignments[assignment.teacherId] += assignment.numHours;
        }
    }

    return (
        <div className="">
            <h1>Course ID: {courseId}</h1>
            <h1>List of teachers assigned (in ID): [{courseTeachers.toString()}]</h1>
            <h1>Course's detail teaching hours between teachers: {JSON.stringify(courseAssignments)}</h1>
        </div>
        
    );
}
 
export default AssignmentSearchCourse;
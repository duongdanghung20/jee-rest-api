const AssignmentSearchTeacher = ({assignments, teachers}) => {
    let teacherId = assignments[0].teacherId;
    let teacherFirstName = "";
    let teacherLastName = "";
    let teacherEq = 0;
    let teacherCourses = [];
    let teacherAssignments = {};

    for (let teacher of teachers) {
        if (teacher.id === teacherId) {
            teacherFirstName = teacher.firstName;
            teacherLastName = teacher.lastName;
            teacherEq = teacher.eq;
        }
    }

    for (let assignment of assignments) {
        teacherAssignments[assignment.courseId] = {};   
        if (!(assignment.courseId in teacherCourses)) {
            teacherCourses.push(assignment.courseId);
        }     
    }

    for (let assignment of assignments) {
        teacherAssignments[assignment.courseId][assignment.groupType] = assignment.numHours;        
    }

    console.log(teacherAssignments);

    let totalNumHours = 0;
    for (let assignment of assignments) {
        if (assignment.groupType === "TD") {
            totalNumHours = totalNumHours + assignment.numHours;
        } else if (assignment.groupType === "CM") {
            totalNumHours = totalNumHours + assignment.numHours * 1.5;
        } else if (assignment.groupType === "TP") {
            totalNumHours = totalNumHours + assignment.numHours * teacherEq;
        }
    }
    return (
        <div className="">
            <h1>Teacher ID: {teacherId}</h1>
            <h1>Teacher Name: {teacherFirstName} {teacherLastName}</h1>
            <h1>Teacher Eq: {teacherEq}</h1>
            <h1>Total number of hours (in equivalent ETD): {Math.round(totalNumHours * 100) / 100}</h1>
            <h1>Teacher's list of courses id: [{teacherCourses.toString()}]</h1>
            <h1>Teacher's detailed teaching hours: {JSON.stringify(teacherAssignments)}</h1>
            
        </div>
        
    );
}
 
export default AssignmentSearchTeacher;
import useFetch from "./useFetch";
import AssignmentList from "./AssignmentList";

const AssignmentHome = () => {
    const {data: assignments, isPending, error} = useFetch('http://localhost:8080/gdd');
    const {data: courses, isPending2, error2} = useFetch('http://localhost:8080/scolarite');
    const {data: teachers, isPending3, error3} = useFetch('http://localhost:8080/rh');
    return (
        <div className="home"> 
            { isPending && <p>Loading...</p> }
            { error && <p>{error}</p> }
            { assignments && <AssignmentList assignments={assignments} courses={courses} /> }
        </div>
    );
}
 
export default AssignmentHome;
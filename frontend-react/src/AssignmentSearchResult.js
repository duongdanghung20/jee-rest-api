import useFetch from "./useFetch";
import AssignmentList from "./AssignmentList";
import { useParams } from "react-router-dom";
import AssignmentSearchTeacher from "./AssignmentSearchTeacher";
import AssignmentSearchCourse from "./AssignmentSearchCourse";

const AssignmentSearchResult = () => {
    const {searchtype, pattern} = useParams();
    const {data: assignments, isPending, error} = useFetch(`http://localhost:8080/gdd/${searchtype}/${pattern}`);
    const {data: teachers, isPending_, error_} = useFetch(`http://localhost:8080/rh`);
    const {data: courses, isPending__, error__} = useFetch(`http://localhost:8080/scolarite`);
    if (searchtype.toLowerCase() === "teacherId".toLowerCase() && pattern !== "-1") {
        return (
            <div className="home"> 
                { isPending && <p>Loading...</p> }
                { error && <p>{error}</p> }
                { assignments && <AssignmentSearchTeacher assignments={assignments} teachers={teachers}/> }
            </div>
        );
    }
    else if (searchtype.toLowerCase() === "courseId".toLowerCase()) {
        return (
            <div className="home"> 
                { isPending && <p>Loading...</p> }
                { error && <p>{error}</p> }
                { assignments && <AssignmentSearchCourse assignments={assignments} courses={courses}/> }
            </div>
        );
    }
    else {
        return (
            <div className="home"> 
                { isPending && <p>Loading...</p> }
                { error && <p>{error}</p> }
                { assignments && <AssignmentList assignments={assignments} courses={courses} /> }
            </div>
        );
    }
    
    
}
 
export default AssignmentSearchResult;
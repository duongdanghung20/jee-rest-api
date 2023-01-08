import useFetch from "./useFetch";
import CourseList from "./CourseList";
import { useParams } from "react-router-dom";

const CourseSearchResult = () => {
    const {searchtype, pattern} = useParams();
    const {data: courses, isPending, error} = useFetch(`http://localhost:8080/scolarite/${searchtype}/${pattern}`);
    if (Array.isArray(courses)) {
        return (
            <div className="home"> 
                { isPending && <p>Loading...</p> }
                { error && <p>{error}</p> }
                { courses && <CourseList courses={courses} /> }
            </div>
        );
    }
    else {
        return (
            <div className="home"> 
                { isPending && <p>Loading...</p> }
                { error && <p>{error}</p> }
                { courses && <CourseList courses={[courses]} /> }
            </div>
        );
    }
}
 
export default CourseSearchResult;
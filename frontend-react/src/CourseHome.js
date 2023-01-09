import useFetch from "./useFetch";
import CourseList from "./CourseList";

const CourseHome = () => {
    const {data: courses, isPending, error} = useFetch('http://localhost:8080/scolarite');
    return (
        <div className="home"> 
            { isPending && <p>Loading...</p> }
            { error && <p>{error}</p> }
            { courses && <CourseList courses={courses} /> }
        </div>
    );
}
 
export default CourseHome;
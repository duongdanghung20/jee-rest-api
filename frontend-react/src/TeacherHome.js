import useFetch from "./useFetch";
import TeacherList from "./TeacherList";

const TeacherHome = () => {
    const {data: teachers, isPending, error} = useFetch('http://localhost:8080/rh');
    return (
        <div className="home"> 
            { isPending && <p>Loading...</p> }
            { error && <p>{error}</p> }
            { teachers && <TeacherList teachers={teachers} /> }
        </div>
    );
}
 
export default TeacherHome;
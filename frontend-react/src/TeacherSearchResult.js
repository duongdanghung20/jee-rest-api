import useFetch from "./useFetch";
import TeacherList from "./TeacherList";
import { useParams } from "react-router-dom";

const TeacherSearchResult = () => {
    const {searchtype, pattern} = useParams();
    const {data: teachers, isPending, error} = useFetch(`http://localhost:8080/rh/${searchtype}/${pattern}`);
    if (Array.isArray(teachers)) {
        return (
            <div className="home"> 
                { isPending && <p>Loading...</p> }
                { error && <p>{error}</p> }
                { teachers && <TeacherList teachers={teachers} /> }
            </div>
        );
    }
    else {
        return (
            <div className="home"> 
                { isPending && <p>Loading...</p> }
                { error && <p>{error}</p> }
                { teachers && <TeacherList teachers={[teachers]} /> }
            </div>
        );
    }
}
 
export default TeacherSearchResult;
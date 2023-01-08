import { useHistory } from "react-router-dom";

const AddBtn = () => {
    const history = useHistory()

    const handleClick = () => {
        history.push('/add');
    }

    return (
        <div className="add-btn">
            <button onClick={handleClick}>+ Add</button>
        </div>
    );
}
 
export default AddBtn;
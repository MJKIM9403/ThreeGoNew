const deleteBtn = document.getElementById('delete-btn');
if(deleteBtn){
    deleteBtn.addEventListener('click', function () {
        let id = document.getElementById('board-id').value;
        fetch(`/api/board/${id}`, {
            method: 'DELETE'
        }).then(()=>{
            alert("삭제 완료");
            location.replace('/board');
        })
    });
}



const modifyBtn = document.getElementById('modify-btn');
if (modifyBtn) {
    modifyBtn.addEventListener('click', function () {
        let params = new URLSearchParams(window.location.search)
        let id = params.get('bid');

        console.log("Clicked modify button. bid:", id);

        // if (!id) {
        //     alert("수정할 글을 찾을 수 없습니다.");
        //     return;
        // }

        fetch(`/api/board/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                btitle: document.getElementById('b_title').value,
                bcontent: document.getElementById('b_content').value
            })
        }).then(() => {
            alert("수정 완료");
            location.replace(`/board/${id}`);
        })
    });
}

const createBtn =  document.getElementById('create-btn');

if(createBtn){
    createBtn.addEventListener('click', function (){

        fetch('/api/board', {
            method: 'POST',
            headers : {
                "Content-Type" : "application/json",
            },
            body : JSON.stringify({
                userid: document.getElementById('u_id').value,
                username: document.getElementById('u_name').value,
                btitle : document.getElementById('b_title').value,
                bcontent : document.getElementById('b_content').value
            })
        }).then(()=>{
            alert("등록 완료");
            location.replace("/board");
        })
    });
}
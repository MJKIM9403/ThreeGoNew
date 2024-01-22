const deleteBtn = document.getElementById('delete-btn');
if(deleteBtn){
    deleteBtn.addEventListener('click', function () {
        let id = document.getElementById('board-id').value;
        fetch(`/api/board/b_id=${id}`, {
            method: 'DELETE'
        }).then(()=>{
            alert("삭제 완료");
            location.replace('/board');
        })
    });
}


// const modifyBtn = document.getElementById('modify-btn');
// if(modifyBtn) {
//     modifyBtn.addEventListener('click', function () {
//         let id = document.getElementById('board-id').value;
//         console.log(id);
//
//
//         if (!id) {
//             alert("수정할 글을 찾을 수 없습니다.");
//             return;
//         }
//
//         fetch(`/api/board/b_id=${id}`, {
//             method : 'PUT',
//             headers : {
//                 "Content-Type" : "application/json",
//             },
//             body : JSON.stringify({
//                 title : document.getElementById('b_title').value,
//                 content : document.getElementById('b_content').value
//             })
//         }).then(()=>{
//             alert("수정 완료");
//             location.replace(`/board/b_id=${id}`);
//         })
//     });
// }

document.addEventListener('DOMContentLoaded', function () {
    const modifyBtn = document.getElementById('modify-btn');
    if (modifyBtn) {
        modifyBtn.addEventListener('click', function () {
            let id = new URLSearchParams(window.location.search).get('bId');

            console.log("Clicked modify button. bId:", id);

            if (!id) {
                alert("수정할 글을 찾을 수 없습니다.");
                return;
            }

            fetch(`/api/board/b_id=${id}`, {
                method: 'PUT',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    title: document.getElementById('b_title').value,
                    content: document.getElementById('b_content').value
                })
            }).then(() => {
                alert("수정 완료");
                location.replace(`/board/b_id=${id}`);
            })
        });
    }
});

const createBtn =  document.getElementById('create-btn');

if(createBtn){
    createBtn.addEventListener('click', function (){

        fetch('/api/board', {
            method: 'POST',
            headers : {
                "Content-Type" : "application/json",
            },
            body : JSON.stringify({
                title : document.getElementById('b_title').value,
                content : document.getElementById('b_content').value
            })
        }).then(()=>{
            alert("등록 완료");
            location.replace("/board");
        })
    });
}
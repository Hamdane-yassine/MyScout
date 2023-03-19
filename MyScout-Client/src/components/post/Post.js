const Post = (props) => {
  const { post, onItemClicked } = props;

  const selectPost = () => {
    onItemClicked(post);
  };

  return (
    <div className="post" onClick={selectPost}>
      <video controls width="100%">
      <source src={`http://localhost:8005${post.post_content}`} alt={`${post.post_created_by} - ${post.post_created_date}`} type="video/webm" />
      <source src={`http://localhost:800(${post.post_content}`} alt={`${post.post_created_by} - ${post.post_created_date}`} type="video/mp4"/>
      Sorry, your browser doesn't support videos.
    </video>
    </div>
    
  );
};
export default Post;
namespace Models
{
    public class Protocol
    {
        public long Id { get; set; }

        public bool IsDraft { get; set; }

        public string? ReviewComment { get; set; }

        public bool IsClosed { get; set; }

        public DateTime ClosedAt { get; set; }

        public DateTime CreatedOrEdited { get; set; }

        public long userId { get; set; }

        public required User User { get; set; }    
        
        public ICollection<AdditionalUser>? AdditionalUser { get; set; }

    }
}
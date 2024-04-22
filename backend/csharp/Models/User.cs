namespace Models
{
    public class User
    {
        public long Id { get; set; }

        public required string Username { get; set; }

        public string? FirstName { get; set; }

        public string? LastName { get; set; }

        public required string Email { get; set; }

        public required string Password { get; set; }

        public DateTime? LastPasswordChangeDate { get; set; }

        public bool? PasswordChangeRequired { get; set; }

        public DateTime CreatedOrEdited { get; set; }

        public long organizationId { get; set; }

        public Organization? Organization { get; set; }

        public ICollection<Protocol>? Protocols { get; set; }
        
        public ICollection<AdditionalUser>? AdditionalUser { get; set; }
    }
}

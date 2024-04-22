namespace Models
{
    public class Organization
    {
        public long Id { get; set; }

        public long parentId { get; set; }

        public required string Name { get; set; }

        public string? Address { get; set; }

        public string? PostalCode { get; set; }

        public string? City { get; set; }

        public string? Country { get; set; }

        public required string OrganizationType { get; set; }

        public DateTime CreatedOrEdited { get; set; }

        public ICollection<User>? Users { get; set; }
    }
}